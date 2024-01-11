import { cn } from "@/utils";
import { Cpu } from "lucide-react";
import React, { memo } from "react";

interface DeviceProps extends React.ComponentPropsWithoutRef<"button"> {
	device: { id: number; name: string; address: string; numMeasures: number };
}

const DeviceCard = ({ device, className, ...props }: DeviceProps) => {
	return (
		<button
			className={cn(
				"rounded-xl border-2 relative group hover:border-primary transition-all  shadow-xl bg-zinc-50 flex flex-col justify-around p-4 cursor-pointer",
				className
			)}
			{...props}
		>
			<Cpu className="absolute top-2 right-2 transition-all group-hover:text-primary w-10 h-10 text-zinc-300" />

			<h3 className="text-lg text-left">
				<strong>{device.name} </strong>
			</h3>
			<div className="flex flex-col">
				<p className="text-left">
					<strong>Indirizzo:</strong> {device.address}
				</p>
				<p className="text-left">
					<strong>Numero misurazioni: </strong> {device.numMeasures}
				</p>
			</div>
		</button>
	);
};

export default memo(DeviceCard);
