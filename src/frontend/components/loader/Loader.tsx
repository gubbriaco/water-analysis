import { CircularProgress } from "@nextui-org/react";
import React from "react";

const Loader = ({ isVisible }: { isVisible: boolean }) => {
	if (!isVisible) return null;

	return (
		<div className="fixed backdrop-blur-sm inset-0 z-50 flex justify-center items-center bg-black/20">
			<CircularProgress />
		</div>
	);
};

export default Loader;
