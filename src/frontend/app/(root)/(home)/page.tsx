"use client";
import DeviceCard from "@/components/cards/DeviceCard";
import Line from "@/components/chart/Line";
import Loader from "@/components/loader/Loader";
import TableDevice from "@/components/table/TableDevice";
import { fetcher } from "@/lib/fetcher";
import { IDevice } from "@/model/interfaces";
import { useEffect, useState } from "react";
import useSWR from "swr";

export default function Home() {
	const [device, setDevice] = useState<IDevice>();

	const { data, isLoading } = useSWR<IDevice[]>("/api/device", fetcher, {
		refreshInterval: 15000,
	});

	useEffect(() => {
		if (data) {
			setDevice(data[Math.floor(Math.random() * data.length)]);
		}
	}, [data]);

	return (
		<>
			<div className="space-y-8 relative">
				<h1 className="font-bold text-3xl">
					Analysis of the device{" "}
					<strong className="text-primary"> {device?.name}</strong>
				</h1>

				<div className="flex gap-8">
					<DeviceCard
						device={{
							id: device?.id ?? 0,
							name: device?.name ?? "Nessun dispositivo disponibile",
							address: device?.address ?? "",
							numMeasures: device?.listaMisuration?.length ?? 0,
						}}
						className="w-full cursor-auto hover:border-default "
					/>

					<div className="">
						<Line />
					</div>
				</div>

				{device?.listaMisuration && device?.listaMisuration?.length > 0 ? (
					<div className="">
						<TableDevice measurements={device?.listaMisuration ?? []} />
					</div>
				) : (
					<div className="text-center flex items-center justify-center h-60">
						There are no measurements for this device
					</div>
				)}
			</div>
			<Loader isVisible={isLoading} />
		</>
	);
}
