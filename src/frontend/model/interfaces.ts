export interface IDevice {
	id: number;
	name: string;
	address: string;
	listaMisuration: IMisuration[];
}

export interface IMisuration {
	id: number;
	date: string;
	temperature: number;
	dissolvedMetal: number;
	ph: number;
}
