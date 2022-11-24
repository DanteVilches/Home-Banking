const { createApp } = Vue;

createApp({
	data() {
		return {
			clients: [],
			client: {},
			backendJson: {},
			inputName: "",
			inputLastName: "",
			inputEmail: "",
			url: "http://localhost:8080/clients",
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
					this.backendJson = data.data;

					this.clients = data.data._embedded.clients;
				})
				.catch((error) => console.log(error));
		},
		addClient() {
			this.client = {
				clientName: this.inputName,
				clientLastName: this.inputLastName,
				clientEmail: this.inputEmail,
			};
			if (
				this.inputName != "" &&
				this.inputLastName != "" &&
				this.inputEmail != "" &&
				this.inputEmail.includes("@" && ".")
			) {
				this.postClient(this.client);
				this.inputEmail = "";
				this.inputLastName = "";
				this.inputName = "";
				Swal.fire({
					icon: "success",
					title: "Ha agregado un nuevo cliente",
				});
				this.loadData();
			} else {
				Swal.fire({
					icon: "error",
					title: "Los campos están vacíos",
					text: "Por favor rellene los campos",
				});
			}
		},
		postClient(client) {
			axios.post(this.url, client);
		},
	},
	computed: {},
}).mount("#app");
