"use client";
import React, { Component, ComponentProps, PropsWithChildren } from "react";
import {
	Modal,
	ModalContent,
	ModalHeader,
	ModalBody,
	ModalFooter,
	Button,
	useDisclosure,
} from "@nextui-org/react";
import DeviceCard from "../cards/DeviceCard";
import TableDevice from "../table/TableDevice";
import Line from "../chart/Line";
import { IDevice } from "@/model/interfaces";

interface DeviceDetailCardProps {
	device: IDevice;
}

export default function DeviceDetailCard({ device }: DeviceDetailCardProps) {
	const { isOpen, onOpen, onOpenChange } = useDisclosure();

	return (
		<>
			<DeviceCard
				device={{
					id: device.id,
					name: device.name,
					address: device.address,
					numMeasures: device.listaMisuration.length,
				}}
				onClick={onOpen}
				className="flex-grow basis-1/5 aspect-[3/4] max-h-[300px]"
			/>
			<Modal
				isOpen={isOpen}
				onOpenChange={onOpenChange}
				size="5xl"
				placement="top"
				classNames={{
					base: "bg-[#f5f5f5] max-w-[1280px]",
				}}
			>
				<ModalContent className="pb-6">
					{(onClose) => (
						<>
							<ModalHeader className="flex gap-1 font-bold text-2xl">
								{device.name}
							</ModalHeader>
							<ModalBody>
								<div className="flex gap-8">
									<TableDevice measurements={device?.listaMisuration ?? []} />

									<div className="space-y-2">
										<Line />
										<Line />
										<Line />
									</div>
								</div>
							</ModalBody>
						</>
					)}
				</ModalContent>
			</Modal>
		</>
	);
}
