Vue.createApp({
	data() {
		return {
			Localtime: new Date().toJSON().slice(0, 10).replace(/-/g, "/"),
			url: "http://localhost:8080/api/clients/current",
			client: {},
			clientName: "",
			accounts: [],
			chosenAccountOrigin: "",
			chosenAccountDestination: "",
			accountDestination: "",
			chosenAccount: "",
			amountBetweenAccounts: "",
			amountToOthers: "",
			descriptionToOthers: "",
			descriptionBetweenAccounts: "",
			logo: "./images/bank logo.png",
		};
	},
	created() {
		this.loadData();
		if (localStorage.getItem("dark-mode") === "true") {
			this.logo = "./images/bank logo.png";
		} else {
			this.logo = "./images/bank logo black.png";
		}
		document.addEventListener("DOMContentLoaded", function () {
			let modeSwitch = document.querySelector(".mode-switch");

			modeSwitch.addEventListener("click", function () {
				document.documentElement.classList.toggle("dark");
				modeSwitch.classList.toggle("active");

				if (document.documentElement.classList.contains("dark")) {
					localStorage.setItem("dark-mode", "true");
				} else {
					localStorage.setItem("dark-mode", "false");
				}
			});

			if (localStorage.getItem("dark-mode") === "true") {
				document.documentElement.classList.add("dark");
				modeSwitch.classList.add("active");
				this.logo = "./images/bank logo.png";
			} else {
				document.documentElement.classList.remove("dark");
				modeSwitch.classList.remove("active");
				this.logo = "./images/bank logo black.png";
			}
		});
	},

	methods: {
		loadData() {
			axios
				.get(this.url)
				.then((data) => {
					this.client = data.data;
					this.accounts = this.client.accountDTO.sort((a, b) => a.id - b.id);

					this.loans = this.client.loans.sort((a, b) => a.id - b.id);

					this.clientName = `${this.client.firstName} ${this.client.lastName}`;
				})
				.catch((error) => console.log(error));
		},
		shuffle() {
			return this.arrayOfColours.sort(() => Math.random() - 0.5);
		},
		formatDate(accountDate) {
			const date = new Date(accountDate);
			return date.toDateString().slice(3);
		},
		formatTime(accountDate) {
			const date = new Date(accountDate);
			let minutes =
				date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes();
			return date.getHours() + ":" + minutes;
		},
		formatCurrency(money) {
			let USDollar = new Intl.NumberFormat("en-US", {
				style: "currency",
				currency: "USD",
			});

			return USDollar.format(money);
		},

		changeLogoColor() {
			if (document.documentElement.classList.contains("dark")) {
				this.logo = "./images/bank logo black.png";
			} else {
				this.logo = "./images/bank logo.png";
			}
		},
		logOut() {
			axios
				.post("/api/logout")
				.then(
					(response) => (window.location.href = "./index.html"),
					console.log("signed out!!!")
				);
		},
		transferToOThers() {
			if (this.chosenAccount && this.amountToOthers && this.accountDestination) {
				if (this.accountDestination.includes("VIN")) {
					Swal.fire({
						text: `You are about to transfer  $${this.amountToOthers} from your account ${this.chosenAccount} to ${this.accountDestination}`,
						icon: "info",
						showCancelButton: true,
						confirmButtonColor: "#3085d6",
						cancelButtonColor: "#d33",
						confirmButtonText: "Yes, Transfer",
					}).then((result) => {
						if (result.isConfirmed) {
							axios
								.post(
									"/api/clients/current/transactions",
									`amount=${this.amountToOthers}&description=${this.descriptionToOthers}&originAccount=${this.chosenAccount}&destinationAccount=${this.accountDestination}`
								)
								.then((response) => {
									Swal.fire("Transfered!", "Your money has been transfered", "success");
								})
								.catch((error) => {
									console.log("error from post");
									console.log(error);
									if (error.response.status == 403) {
										Swal.fire({
											icon: "error",
											title: error.response.data,
											text: "Please choose a valid amount",
										});
									}
								});
						}
					});
				} else {
					Swal.fire({
						icon: "error",
						title: "Destination account not valid",
						text: "Please correct the account destination field",
					});
				}
			} else {
				Swal.fire({
					icon: "error",
					title: "Fields are empty",
					text: "Please complete the fields",
				});
			}
		},
		transfer() {
			if (
				this.chosenAccountOrigin ||
				this.amountBetweenAccounts ||
				this.chosenAccountDestination
			) {
				Swal.fire({
					text: `You are about to transfer  $${this.amountBetweenAccounts} from your account ${this.chosenAccountOrigin} to ${this.chosenAccountDestination}`,
					icon: "info",
					showCancelButton: true,
					confirmButtonColor: "#3085d6",
					cancelButtonColor: "#d33",
					confirmButtonText: "Yes, Transfer",
				}).then((result) => {
					if (result.isConfirmed) {
						axios
							.post(
								"/api/clients/current/transactions",
								`amount=${this.amountBetweenAccounts}&description=${this.descriptionBetweenAccounts}&originAccount=${this.chosenAccountOrigin}&destinationAccount=${this.chosenAccountDestination}`
							)
							.then((response) => {
								Swal.fire("Transfered!", "Your money has been transfered", "success");
							})
							.catch((error) => {
								console.log("error from post");
								console.log(error);
								if (error.response.status == 403) {
									Swal.fire({
										icon: "error",
										title: error.response.data,
										text: "Please choose a valid amount",
									});
								}
							});
					}
				});
			} else {
				Swal.fire({
					icon: "error",
					title: "Fields are empty",
					text: "Please complete the fields",
				});
			}
		},
	},

	computed: {},
}).mount("#app");
