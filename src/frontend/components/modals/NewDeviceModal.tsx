"use client";
import {
	Modal,
	ModalContent,
	ModalHeader,
	ModalBody,
	ModalFooter,
	Button,
	useDisclosure,
	Input,
	Tooltip,
} from "@nextui-org/react";
import { Plus } from "lucide-react";
import {useState } from "react";
import fetchJson from "../../lib/fetchJSON";

export default function NewDeviceModal() {
	const { isOpen, onOpen, onOpenChange,onClose } = useDisclosure();
	const [open, setOpen] = useState(false);
	const [name, setName] = useState("");
	const [address, setAddress] = useState("");
	const [environment, setEnvironment] = useState("");

	const onSubmit = async () => {
		await fetchJson("api/device", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({
				name: name,
				address: address,
				environment: environment
			}),
		});
		onClose()

	};

	const onOpenChangeMiddleware = () => {
		setName("");
		setAddress("");
		setEnvironment("");
		onOpenChange();
	};

	return (
		<>
			<Tooltip
				content="Add new device"
				placement="top-start"
				size="sm"
				color="primary"
			>
				<Button
					color="primary"
					size="sm"
					onPress={onOpen}
					isIconOnly
					radius="full"
				>
					<Plus size={18} />
				</Button>
			</Tooltip>
			<Modal isOpen={isOpen} onOpenChange={onOpenChangeMiddleware}>
				<ModalContent>
					{(onClose) => (
						<>
							<ModalHeader className="flex flex-col gap-1">
								Aggungi un nuovo dispositivo
							</ModalHeader>
							<ModalBody>
								<div className="space-y-6 pb-10">
									<Input
										label="Nome"
										placeholder="Aggiungi nome dispositivo"
										required
										value={name}
										onChange={(e) => setName(e.target.value)}
									/>
									<Input
										label="Address"
										placeholder="Aggiungi address dispositivo"
										required
										value={address}
										onChange={(e) => setAddress(e.target.value)}
									/>
									<Input
										label="Environment"
										placeholder="Aggiungi environment del dispositivo"
										required
										value={environment}
										onChange={(e) => setEnvironment(e.target.value)}
									/>
								</div>
							</ModalBody>
							<ModalFooter>
								<Button color="danger" variant="light" onPress={onClose}>
									Chiudi
								</Button>
								<Button color="primary" onPress={onSubmit}>
									Aggiungi
								</Button>
							</ModalFooter>
						</>
					)}
				</ModalContent>
			</Modal>
		</>
	);
}
