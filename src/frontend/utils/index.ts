import clsx, { ClassValue } from "clsx";
import React from "react";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
	return twMerge(clsx(inputs));
}

export function getChildrenOnDisplayName<T>(
	children: React.ReactNode,
	displayName?: string
) {
	const childrenArray = React.Children.toArray(children);

	return childrenArray.filter((child) => {
		const childElement = child as React.ReactElement & {
			type: { displayName?: string };
		};
		return displayName ? childElement.type.displayName === displayName : true;
	}) as T;
}

export const removeDuplicateObjects = (array: any[], property: string) => {
	const uniqueIds: any[] = [];

	const unique = array.filter((element) => {
		const isDuplicate = uniqueIds.includes(element[property]);

		if (!isDuplicate) {
			uniqueIds.push(element[property]);

			return true;
		}

		return false;
	});

	return unique;
};
