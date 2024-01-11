import Footer from "@/components/footer/Footer";
import Header from "@/components/header/Header";
import React, { PropsWithChildren } from "react";

const Layout = ({ children }: PropsWithChildren) => {
	return (
		<div className="flex h-full min-h-[100vh] flex-col">
			<Header />
			<main className="flex-grow container py-10">{children}</main>
			<Footer />
		</div>
	);
};

export default Layout;
