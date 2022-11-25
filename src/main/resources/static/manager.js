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
				this.inputEmail != ""
			) {
				if (this.inputEmail.includes("@") && this.inputEmail.includes(".")) {
					this.postClient(this.client);
					this.inputEmail = "";
					this.inputLastName = "";
					this.inputName = "";
					Swal.fire({
						icon: "success",
						title: "A new client has been added",
					});
				} else {
					Swal.fire({
						icon: "error",
						title: "Email is not valid",
						text: "Please correct the email format",
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
		postClient(client) {
			axios.post(this.url, client).then((response) => this.loadData());
		},
		deleteClient(client) {
			let idIndex = client._links.client.href.lastIndexOf("/");
			let id = client._links.client.href.substring(idIndex + 1);

			Swal.fire({
				title: "Are you sure that you want to delete this client?",
				text: "You won't be able to revert this",
				icon: "error",
				showCancelButton: true,
				confirmButtonColor: "#3085d6",

				cancelButtonColor: "#d33",
				confirmButtonText: "Yes, delete this client",
			}).then((result) => {
				if (result.isConfirmed) {
					Swal.fire("Deleted!", "The client has been deleted.", "success");
					axios
						.delete(`http://localhost:8080/clients/${id}`)
						.then((response) => this.loadData());
				}
			});
		},
		async modifyClient(client) {
			let idIndex = client._links.client.href.lastIndexOf("/");
			let id = client._links.client.href.substring(idIndex + 1);
			const { value: formValues } = await Swal.fire({
				title: "Edit Client",
				html:
					`<label>First Name<input id="swal-input1" class="swal2-input" value=${client.clientName}></label>` +
					`<label>Last Name<input id="swal-input2" class="swal2-input" value=${client.clientLastName}></label>` +
					`<label>Client Email<input type="email "id="swal-input3" class="swal2-input" value=${client.clientEmail}></></label>`,
				focusConfirm: false,
				confirmButtonText: "Save",
				confirmButtonColor: "#3085d6",
				showCancelButton: true,
				allowEnterKey: true,
				width: "39rem",
				preConfirm: () => {
					return [
						document.getElementById("swal-input1").value,
						document.getElementById("swal-input2").value,
						document.getElementById("swal-input3").value,
					];
				},
			});
			let inputSwalFirstName = document.getElementById("swal-input1").value;
			let inputSwalLastName = document.getElementById("swal-input2").value;
			let inputSwalEmail = document.getElementById("swal-input3").value;
			if (formValues) {
				this.client = {
					clientName: inputSwalFirstName,
					clientLastName: inputSwalLastName,
					clientEmail: inputSwalEmail,
				};
				if (
					inputSwalFirstName != "" &&
					inputSwalLastName != "" &&
					inputSwalEmail != ""
				) {
					if (inputSwalEmail.includes("@") && inputSwalEmail.includes(".")) {
						axios
							.put(`http://localhost:8080/clients/${id}`, this.client)
							.then((response) => this.loadData());

						Swal.fire({
							icon: "success",
							title: "The client has been edited",
						});
					} else {
						Swal.fire({
							icon: "error",
							title: "Email is not valid",
							text: "Please correct the email format",
						});
					}
				} else {
					Swal.fire({
						icon: "error",
						title: "Fields are empty",
						text: "Please complete the fields",
					});
				}
			}
		},
		async modifyOne(attribute, dataType, client) {
			let idIndex = client._links.client.href.lastIndexOf("/");
			let id = client._links.client.href.substring(idIndex + 1);
			const { value: formValues } = await Swal.fire({
				title: `Edit ${dataType}`,
				html: `<label>${dataType}<input id="swal-input" class="swal2-input" value=${attribute}></label>`,
				focusConfirm: false,
				confirmButtonText: "Save",
				confirmButtonColor: "#3085d6",
				showCancelButton: true,
				allowEnterKey: true,
				preConfirm: () => {
					return document.getElementById("swal-input").value;
				},
			});

			let inputSwal = document.getElementById("swal-input").value;
			const enviar = () => {
				axios
					.patch(`http://localhost:8080/clients/${id}`, this.client)
					.then((response) => this.loadData());
				Swal.fire({
					icon: "success",
					title: `The ${dataType} has been edited`,
				});
			};
			if (formValues) {
				switch (dataType) {
					case "First Name":
						this.client = {
							clientName: inputSwal,
						};
						enviar();
						break;

					case "Last Name":
						this.client = {
							clientLastName: inputSwal,
						};
						enviar();
						break;

					case "Email":
						this.client = {
							clientEmail: inputSwal,
						};
						enviar();
						break;
					default:
				}
			}
		},
	},
	computed: {},
}).mount("#app");
