const { createApp } = Vue;

const app = createApp({
	data() {
		return {
			accounts: [],
			account: [],
			id: "",
			transactions: [],
			accountName: "",
			clientName: "",
			accountBalance: "",
			logo: "./images/bank logo.png",
			Localtime: new Date().toJSON().slice(0, 10).replace(/-/g, "/"),
			type: "",
		};
	},

	created() {
		let queryString = location.search;
		let params = new URLSearchParams(queryString);
		this.id = params.get("id");
		this.loadData();
		this.loadAllAccounts();
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
				this.logo = "./images/bank logo.png";
				document.documentElement.classList.add("dark");
				modeSwitch.classList.add("active");
			} else {
				console.log("hola");
				this.logo = "./images/bank logo black.png";
				document.documentElement.classList.remove("dark");
				modeSwitch.classList.remove("active");
			}
		});
	},
	methods: {
		loadData() {
			axios
				.get("http://localhost:8080/api/accounts/" + this.id)
				.then((data) => {
					this.account = data.data;
					this.accountName = this.account.accountNumber;
					this.accountBalance = this.account.accountBalance;
					this.transactions = this.account.transactionDTO.sort(
						(a, b) => a.transactionID - b.transactionID
					);
				})

				.catch((error) => console.log(error));
		},
		loadAllAccounts() {
			axios.get("http://localhost:8080/api/clients/1").then((json) => {
				this.accounts = json.data.accountDTO.sort(
					(a, b) => a.accountId - b.accountId
				);
				this.clientName = json.data.firstName + " " + json.data.lastName;
			});
		},
		formatDate(transactionDate) {
			const date = new Date(transactionDate);
			return date.toDateString().slice(3);
		},
		formatTime(transactionDate) {
			const date = new Date(transactionDate);
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
	},

	computed: {},
}).mount("#app");
