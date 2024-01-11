import React from "react";
import { ResizableBox as ReactResizableBox } from "react-resizable";

import "react-resizable/css/styles.css";

interface ResizableBoxProps {
	children: React.ReactNode;
	width?: number;
	height?: number;
	resizable?: boolean;
	style?: React.CSSProperties;
	className?: string;
}

export default function ResizableBox({
	children,
	width = 600,
	height = 300,
	resizable = true,
	style = {},
	className = "",
}: ResizableBoxProps) {
	return (
		<div
			style={{
				display: "inline-block",
				width: "auto",
				background: "white",
				padding: ".5rem",
				...style,
			}}
			className="rounded-xl shadow-xl border"
		>
			{resizable ? (
				<ReactResizableBox width={width} height={height}>
					<div
						style={{
							width: "100%",
							height: "100%",
						}}
						className={className}
					>
						{children}
					</div>
				</ReactResizableBox>
			) : (
				<div
					style={{
						width: `${width}px`,
						height: `${height}px`,
					}}
					className={className}
				>
					{children}
				</div>
			)}
		</div>
	);
}
