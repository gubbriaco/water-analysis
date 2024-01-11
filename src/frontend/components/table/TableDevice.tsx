"use client";

import React from "react";
import {
	Table,
	TableHeader,
	TableColumn,
	TableBody,
	TableRow,
	TableCell,
} from "@nextui-org/react";
import { IMisuration } from "@/model/interfaces";

interface TableDeviceProps {
	measurements?: IMisuration[];
}

export default function TableDevice({ measurements }: TableDeviceProps) {
	return (
		<Table aria-label="Example static collection table">
			<TableHeader>
				<TableColumn>Temp.</TableColumn>
				<TableColumn>TDS</TableColumn>
				<TableColumn>pH</TableColumn>
				<TableColumn>Data</TableColumn>
				<TableColumn>Ora</TableColumn>
			</TableHeader>
			<TableBody>
				{measurements?.map((misuration, index) => {
					return (
						<TableRow key={index} className="hover:bg-zinc-50">
							<TableCell>{misuration.temperature}</TableCell>
							<TableCell>{misuration.dissolvedMetal}</TableCell>
							<TableCell>{misuration.ph}</TableCell>

							<TableCell>
								{new Date(misuration.date).toLocaleDateString()}
							</TableCell>
							<TableCell>
								{new Date(misuration.date).toLocaleTimeString()}
							</TableCell>
						</TableRow>
					);
				}) ?? []}
			</TableBody>
		</Table>
	);
}
