Vue.createApp({
	data() {
		return {
			Localtime: new Date().toJSON().slice(0, 10).replace(/-/g, "/"),
			url: "/api/clients/current",
			client: {},
			clientName: "",
			accounts: [],
			loans: [],
			accountDestination: "",
			chosenAccount: "",
			chosenLoan: "",
			loanSelected: {},
			paymentsOfLoanSelected: [],
			clientLoans: [],
			amount: "",
			maxAmount: "Max Amount allowed",
			chosenPayment: "",
			maxAmountNoFormat: "",
			percentageOfLoan: 0,
			amountOfEachPayment: 0,
			disabledOption: false,
			disabledOption2: false,
			logo: "./images/bank logo.png",
		};
	},
	created() {
		this.loadData();
		this.loadLoans();
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
					this.clientName = `${this.client.firstName} ${this.client.lastName}`;
				})
				.catch((error) => console.log(error));
		},
		loadLoans() {
			axios
				.get("/api/loans")
				.then((data) => {
					this.loans = data.data;
				})
				.catch((error) => {
					console.log("loans error");
					console.log(error);
				});
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
		getALoan() {
			console.log(this.amount);
			if (
				this.amount &&
				this.chosenLoan &&
				this.chosenPayment &&
				this.chosenAccount
			) {
				Swal.fire({
					text: `You are about to get a ${
						this.loanSelected.name
					} loan with an amount of ${this.formatCurrency(this.amount)} in ${
						this.chosenPayment
					} payments and the amount will be transfered to your account ${
						this.chosenAccount
					}, do you wanna get this loan?`,
					icon: "info",
					showCancelButton: true,
					confirmButtonColor: "#3085d6",
					cancelButtonColor: "#d33",
					confirmButtonText: "Yes, get this loan",
				}).then((result) => {
					if (result.isConfirmed) {
						axios
							.post("/api/loans", {
								id: this.chosenLoan,
								amount: this.amount,
								payments: this.chosenPayment,
								account: this.chosenAccount,
							})
							.then((response) => {
								Swal.fire(
									"Success!",
									"the money  from the loan has been transfered to your account",
									"success"
								);
							})
							.catch((error) => {
								console.log("error from post");
								console.log(error);
								if (error.response.status == 403) {
									Swal.fire({
										icon: "error",
										title: error.response.data,
										text: "Please correct the fields",
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
		showPayments(event) {
			this.loanSelected = this.loans.find((loan) => loan.id == event.target.value);
			this.paymentsOfLoanSelected = this.loanSelected.payments;
			this.maxAmount = this.formatCurrency(this.loanSelected.maxAmount);
			this.maxAmountNoFormat = this.loanSelected.maxAmount;
			this.percentageOfLoan = this.loanSelected.interestPercentage;
			this.disabledOption = true;
		},
		paymentSelected(event) {
			this.amountOfEachPayment = this.formatCurrency(
				this.amount / parseInt(event.target.value)
			);
			this.disabledOption2 = true;
		},
	},

	computed: {
		calculateAmount() {
			if (this.amount === "" || this.chosenPayment === "") {
				this.amountOfEachPayment = 0;
			} else {
				this.amountOfEachPayment = this.formatCurrency(
					(this.amount * (this.percentageOfLoan * 0.01 + 1)) / this.chosenPayment
				);
			}
		},
	},
}).mount("#app");
