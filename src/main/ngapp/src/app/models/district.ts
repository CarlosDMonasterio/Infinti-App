import {School} from "./school";

export class District {
    id: number;
    label: string;
    description: string;
    schoolCount: number;
    schools: School[];
}