Vue.createApp({
	data() {
		return {
			Localtime: new Date().toJSON().slice(5, 10).replace(/-/g, "/"),
			url: "http://localhost:8080/api/clients/1",
			client: {},
			clientName: "",
			accounts: [],
		};
	},
	created() {
		this.loadData();
	},

	methods: {
		loadData() {
			axios
				.get(this.url)
				.then((data) => {
					this.client = data.data;

					this.accounts = this.client.accountDTO;
					this.clientName = `${this.client.clientName} ${this.client.clientLastName}`;
					console.log(this.accounts);
				})
				.catch((error) => console.log(error));
		},
	},

	computed: {},
}).mount("#app");

document.addEventListener("DOMContentLoaded", function () {
	let modeSwitch = document.querySelector(".mode-switch");

	modeSwitch.addEventListener("click", function () {
		document.documentElement.classList.toggle("dark");
		modeSwitch.classList.toggle("active");
	});

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

	document.querySelector(".messages-btn").addEventListener("click", function () {
		document.querySelector(".messages-section").classList.add("show");
	});

	document
		.querySelector(".messages-close")
		.addEventListener("click", function () {
			document.querySelector(".messages-section").classList.remove("show");
		});
});
