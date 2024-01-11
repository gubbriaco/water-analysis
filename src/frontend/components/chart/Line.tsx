"use client";

import ResizableBox from "@/components/resizable-box/ResizableBox";
import useDemoConfig from "@/hook/useDemoConfig";
import { Button } from "@nextui-org/react";
import React from "react";
import { AxisOptions, Chart } from "react-charts";

export default function Line() {
	const { data, randomizeData } = useDemoConfig({
		series: 1,
		dataType: "time",
	});

	const primaryAxis = React.useMemo<
		AxisOptions<(typeof data)[number]["data"][number]>
	>(
		() => ({
			getValue: (datum) => datum.primary as unknown as Date,
		}),
		[]
	);

	const secondaryAxes = React.useMemo<
		AxisOptions<(typeof data)[number]["data"][number]>[]
	>(
		() => [
			{
				getValue: (datum) => datum.secondary,
			},
		],
		[]
	);

	return (
		<>
			<ResizableBox>
				<Chart
					options={{
						data,
						primaryAxis,
						secondaryAxes,
					}}
				/>
			</ResizableBox>
			<br />

			{/* <div className="w-full flex justify-end">
				<Button
					color="primary"
					size="sm"
					variant="bordered"
					onClick={randomizeData}
				>
					Randomize Data
				</Button>
			</div> */}
		</>
	);
}
