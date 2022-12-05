const { createApp } = Vue;

const app = createApp({
	data() {
		return {
			accounts: [],
			account: [],
			id: "",
			transactions: [],
			accountName: "",
			Localtime: new Date().toJSON().slice(5, 10).replace(/-/g, "/"),
			type: "",
		};
	},

	created() {
		this.loadData();
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
			} else {
				document.documentElement.classList.remove("dark");
				modeSwitch.classList.remove("active");
			}
		});
	},
	methods: {
		loadData() {
			axios
				.get("http://localhost:8080/api/accounts/")
				.then((json) => {
					this.accounts = json.data;
					let queryString = location.search;
					let params = new URLSearchParams(queryString);
					this.id = params.get("id");

					this.account = this.accounts.find(
						(account) => account.accountId == this.id
					);
					this.accountName = this.account.accountNumber;
					this.transactions = this.account.transactionDTO;
				})
				.catch((error) => console.log(error));
		},
	},

	computed: {},
}).mount("#app");
