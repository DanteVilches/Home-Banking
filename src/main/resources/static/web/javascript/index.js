const { createApp } = Vue;

const app = createApp({
	data() {
		return {
			accounts: [],
			account: [],
			id: "",
			user: "",
			password: "",
			firstName: "",
			lastName: "",
			email: "",
			passwordSignUp: "",
			logo: "./images/bank logo.png",
			Localtime: new Date().toJSON().slice(0, 10).replace(/-/g, "/"),
			type: "",
		};
	},

	created() {
		let queryString = location.search;
		let params = new URLSearchParams(queryString);
		this.id = params.get("id");

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
		login() {
			axios
				.post("/api/login", "email=" + this.user + "&password=" + this.password)
				.then((response) => {
					const Toast = Swal.mixin({
						toast: true,
						position: "top-end",
						showConfirmButton: false,
						timer: 3000,
						timerProgressBar: true,
						didOpen: (toast) => {
							toast.addEventListener("mouseenter", Swal.stopTimer);
							toast.addEventListener("mouseleave", Swal.resumeTimer);
						},
					});

					Toast.fire({
						icon: "success",
						title: "Signed in successfully",
					});
					setTimeout(() => {
						if (this.user == "admin@admin") {
							window.location.href = "../manager.html";
						} else {
							window.location.href = "./accounts.html";
						}
					}, 3000);

					console.log("signed in!!!");
				})
				.catch((response) => {
					if (response.response.status == "401") {
						Swal.fire({
							icon: "error",
							title: "Email/Password incorrect",
							text: "Please correct the login info",
						});
					}
				});
		},
		logOut() {
			axios
				.post("/api/logout")
				.then(
					(response) => (window.location.href = "./index.html"),
					console.log("signed out!!!")
				);
		},
		register() {
			axios
				.post(
					"/api/clients",
					"firstName=" +
						this.firstName +
						"&lastName=" +
						this.lastName +
						"&email=" +
						this.email +
						"&password=" +
						this.passwordSignUp
				)
				.then((response) => {
					console.log("registered");
					axios
						.post(
							"/api/login",
							"email=" + this.email + "&password=" + this.passwordSignUp
						)
						.then((response) => {
							window.location.href = "./accounts.html";
						});
				});
		},
	},

	computed: {},
}).mount("#app");

const toggleForm = () => {
	const container = document.querySelector(".container");
	container.classList.toggle("active");
};
