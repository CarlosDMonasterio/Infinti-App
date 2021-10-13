import {User} from "./User";

export class Resource {
    id: number;
    type = "INSTRUMENT";
    label: string;
    description: string;
    created: number;
    costPerHour: number;
    creator: User;
}
