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
			url: "http://localhost:8080/rest/clients",
		};
	},
	created() {
		this.loadData();
	},
	methods: {
		loadData() {
			axios
				.get("http://localhost:8080/api/clients")
				.then((data) => {
					this.backendJson = data.data;

					this.clients = data.data;
					console.log(this.clients);
				})
				.catch((error) => console.log(error));
		},
		addClient() {
			this.client = {
				firstName: this.inputName,
				lastName: this.inputLastName,
				email: this.inputEmail,
			};
			if (this.inputName && this.inputLastName && this.inputEmail) {
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
					/* 	if (client.accountDTO.length != 0) {
						client.accountDTO.forEach((element) => {
							let aux = "http://localhost:8080/rest/accounts/" + element.accountId;
							axios.delete(aux).then((response) => this.loadData());
						});
					} */
					axios
						.delete("http://localhost:8080/rest/clients/" + client.id)
						.then((response) => this.loadData());
				}
			});
		},
		async modifyClient(client) {
			const { value: formValues } = await Swal.fire({
				title: "Edit Client",
				html:
					`<label>First Name<input id="swal-input1" class="swal2-input" value=${client.firstName}></label>` +
					`<label>Last Name<input id="swal-input2" class="swal2-input" value=${client.lastName}></label>` +
					`<label>Client Email<input type="email "id="swal-input3" class="swal2-input" value=${client.email}></></label>`,
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
					firstName: inputSwalFirstName,
					lastName: inputSwalLastName,
					email: inputSwalEmail,
				};
				if (inputSwalFirstName && inputSwalLastName && inputSwalEmail) {
					if (inputSwalEmail.includes("@") && inputSwalEmail.includes(".")) {
						axios
							.put("http://localhost:8080/rest/clients/" + client.id, this.client)
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
			await Swal.fire({
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
			const send = () => {
				axios
					.patch("http://localhost:8080/rest/clients/" + client.id, this.client)
					.then((response) => this.loadData());
				Swal.fire({
					icon: "success",
					title: `The ${dataType} has been edited`,
				});
			};
			const sameValue = () => {
				Swal.fire({
					icon: "warning",
					title: `${dataType} is the same as before`,
					text: "Please change the field",
				});
			};
			const error = () => {
				Swal.fire({
					icon: "error",
					title: `${dataType} is Empty`,
					text: "Please complete the fields",
				});
			};

			switch (dataType) {
				case "First Name":
					if (attribute != inputSwal) {
						if (inputSwal) {
							this.client = {
								firstName: inputSwal,
							};
							send();
						} else {
							error();
						}
					} else {
						sameValue();
					}

					break;

				case "Last Name":
					if (attribute != inputSwal) {
						if (inputSwal) {
							this.client = {
								lastName: inputSwal,
							};
							send();
						} else {
							error();
						}
					} else {
						sameValue();
					}

					break;

				case "Email":
					if (attribute != inputSwal) {
						if (inputSwal.includes("@") && inputSwal.includes(".") && inputSwal) {
							this.client = {
								email: inputSwal,
							};
							send();
						} else {
							Swal.fire({
								icon: "error",
								title: "Email is not valid",
								text: "Please correct the email format",
							});
						}
					} else {
						sameValue();
					}

					break;
				default:
			}
		},
		logOut() {
			axios
				.post("/api/logout")
				.then(
					(response) => (window.location.href = "./web/index.html"),
					console.log("signed out!!!")
				);
		},
	},
	computed: {},
}).mount("#app");
