const { createApp } = Vue;

const app = createApp({
	data() {
		return {
			accounts: [],
			account: [],
			cards: [],
			id: "",
			radioType: "",
			radioColor: "",
			transactions: [],
			accountName: "",
			clientName: "",
			logo: "./images/bank logo.png",
			accountBalance: "",
			Localtime: new Date().toJSON().slice(0, 10).replace(/-/g, "/"),
			type: "",
		};
	},

	created() {
		let queryString = location.search;
		let params = new URLSearchParams(queryString);

		this.id = params.get("id");

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
			} else {
				document.documentElement.classList.remove("dark");
				modeSwitch.classList.remove("active");
			}
		});
	},
	methods: {
		loadData() {
			axios.get("http://localhost:8080/api/clients/current").then((json) => {
				this.cards = json.data.cardDTO.sort((a, b) => a.id - b.id);

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
		logOut() {
			axios
				.post("/api/logout")
				.then(
					(response) => (window.location.href = "./index.html"),
					console.log("signed out!!!")
				);
		},
		createCard() {
			axios
				.post(
					"/api/clients/current/cards",
					"cardType=" + this.radioType + "&cardColor=" + this.radioColor
				)
				.then((response) => {
					Swal.fire("Created!", "", "success");
					this.loadData();
				})
				.catch((error) => {
					if (error.response.status == "400") {
						Swal.fire({
							icon: "error",
							title: "Incorrect Card type/color",
							text: "Please choose one",
						});
					}
					if (error.response.status == "403") {
						Swal.fire({
							icon: "error",
							title: "There's already a card with this type and color",
							text: "Please choose another",
						});
					}
				});
		},
	},

	computed: {},
}).mount("#app");
