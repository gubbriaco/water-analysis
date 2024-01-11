"use client";

import React from "react";
import {
	Navbar,
	NavbarBrand,
	NavbarContent,
	NavbarItem,
	Button,
	NavbarMenu,
	NavbarMenuItem,
	NavbarMenuToggle,
} from "@nextui-org/react";
import { usePathname } from "next/navigation";
import Link from "next/link";

export default function Header() {
	const [isMenuOpen, setIsMenuOpen] = React.useState(false);

	const pathname = usePathname();

	const menuItems = [
		"Profile",
		"Dashboard",
		"Activity",
		"Analytics",
		"System",
		"Deployments",
		"My Settings",
		"Team Settings",
		"Help & Feedback",
		"Log Out",
	];

	return (
		<Navbar
			onMenuOpenChange={setIsMenuOpen}
			className="bg-primary text-white shadow-md"
		>
			<NavbarContent>
				<NavbarMenuToggle
					aria-label={isMenuOpen ? "Close menu" : "Open menu"}
					className="sm:hidden"
				/>
				<NavbarBrand>
					<code> WATER CONNECT</code>
				</NavbarBrand>
			</NavbarContent>

			<NavbarContent className="hidden sm:flex gap-6" justify="center">
				<NavbarItem isActive={pathname === "/"} className="text-lg">
					<Link href="/">Home</Link>
				</NavbarItem>
				<NavbarItem isActive={pathname === "/devices"} className="text-lg">
					<Link href="/devices" aria-current="page">
						Devices
					</Link>
				</NavbarItem>
				<NavbarItem isActive={pathname === "/team"} className="text-lg">
					<Link href="/team">Team</Link>
				</NavbarItem>
			</NavbarContent>
			<NavbarContent justify="end">
				<NavbarItem className="hidden lg:flex">
					<Link href="#">Login</Link>
				</NavbarItem>
				<NavbarItem>
					<Button as={Link} href="#" variant="bordered" className="text-white">
						Sign Up
					</Button>
				</NavbarItem>
			</NavbarContent>
			<NavbarMenu>
				{menuItems.map((item, index) => (
					<NavbarMenuItem key={`${item}-${index}`}>
						<Link
							color={
								index === 2
									? "primary"
									: index === menuItems.length - 1
									? "danger"
									: "foreground"
							}
							className="w-full"
							href="#"
						>
							{item}
						</Link>
					</NavbarMenuItem>
				))}
			</NavbarMenu>
		</Navbar>
	);
}
