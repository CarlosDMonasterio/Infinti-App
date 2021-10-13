import {User} from "./User";
import {Resource} from "./resource";

export class Preset {
    id: number;
    label: string;
    description: string;
    consumables: number;
    laborTime: number;
    resources: Resource[]
    created: number;
    creator: User;

    // initializing constructor
    constructor() {
        this.resources = [];
        this.resources.push(new Resource());
    }
}
