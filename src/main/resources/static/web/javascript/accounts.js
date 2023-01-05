Vue.createApp({
	data() {
		return {
			Localtime: new Date().toJSON().slice(0, 10).replace(/-/g, "/"),
			url: "http://localhost:8080/api/clients/current",
			client: {},
			clientName: "",
			accounts: [],
			loans: [],
			index: "",
			logo: "./images/bank logo.png",
			arrayOfColours: ["#fee4cb", "#e9e7fd", "#ffd3e2", "#c8f7dc", "#d5deff"],
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
		createAccount() {
			Swal.fire({
				title: "Do you want to create a new account?",
				icon: "info",
				showCancelButton: true,
				confirmButtonText: "Create",
			}).then((result) => {
				/* Read more about isConfirmed, isDenied below */
				if (result.isConfirmed) {
					axios.post("/api/clients/current/accounts").then((response) => {
						this.loadData();
					});
					Swal.fire("Created!", "", "success");
				}
			});
		},
	},

	computed: {},
}).mount("#app");
