Vue.createApp({
	data() {
		return {
			Localtime: new Date().toJSON().slice(5, 10).replace(/-/g, "/"),
			url: "http://localhost:8080/api/clients/1",
			client: {},
			clientName: "",
			accounts: [],
			arrayOfColours: ["#fee4cb", "#e9e7fd", "#ffd3e2", "#c8f7dc", "#d5deff"],
		};
	},
	created() {
		this.loadData();
		this.shuffle();
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

			let listView = document.querySelector(".list-view");
			let gridView = document.querySelector(".grid-view");
			let projectsList = document.querySelector(".project-boxes");

			listView.addEventListener("click", function () {
				gridView.classList.remove("active");
				listView.classList.add("active");
				projectsList.classList.remove("jsGridView");
				projectsList.classList.add("jsListView");
			});

			gridView.addEventListener("click", function () {
				gridView.classList.add("active");
				listView.classList.remove("active");
				projectsList.classList.remove("jsListView");
				projectsList.classList.add("jsGridView");
			});
		});
	},

	methods: {
		loadData() {
			axios
				.get(this.url)
				.then((data) => {
					this.client = data.data;
					this.accounts = this.client.accountDTO;
					this.clientName = `${this.client.clientName} ${this.client.clientLastName}`;
				})
				.catch((error) => console.log(error));
		},
		shuffle() {
			return this.arrayOfColours.sort(() => Math.random() - 0.5);
		},
	},

	computed: {},
}).mount("#app");
