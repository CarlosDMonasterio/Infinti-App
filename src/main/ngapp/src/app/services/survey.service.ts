import {Injectable} from '@angular/core';
import {SurveyQuestion} from "../models/survey-question";

@Injectable({
    providedIn: 'root'
})
export class SurveyService {

    private questions: SurveyQuestion[] = [
        {
            label: "Appropriate Attire & ID",
            details: "Does the staff have proper attire, ID and credentials?",
            options: ["Yes", "No"]
        },
        {
            label: "Date Entry Staff",
            details: "Was there sufficient Data Entry Staff to safely operate the test site?",
            options: ["Adequate", "Not Adequate"]
        },
        {
            label: "HCP Staff",
            details: "Was there sufficient Health Care Professionals (HCPs) staff to safely operate the test site?",
            options: ["Adequate", "Not Adequate"]
        },
        {
            label: "Couriers",
            details: "Couriers available and easily contacted for timely specimen pick-up/supply drop offs?",
            options: ["Adequate", "Not Adequate"]
        },
        {
            label: "Grounds Coordinator",
            details: "Was there a Grounds Coordinator present during your assessment?",
            options: ["Yes", "No"]
        },
        {
            label: "Testing Equipment",
            details: "Was there any problems with the testing equipment?",
            options: ["Yes", "No"]
        },
        {
            label: "Proper Mask Type",
            details: "Were all test site staff wearing the correct face mask for their position?",
            options: ["Yes", "No"]
        },
        {
            label: "Mask Compliance",
            details: "Were all test site staff wearing their masks properly (covering nose and mouth) at all times?",
            options: ["Yes", "No"]
        },
        {
            label: "Face Shields / Goggles",
            details: "Were the test site wearing face shields / goggles?",
            options: ["Yes", "No"]
        },
        {
            label: "Shoe / Hair Covers",
            details: "Were all HCPs wearing shoe and hair covers during collection?",
            options: ["Yes", "No"]
        },
        {
            label: "Gowns",
            details: "Were all HCPs wearing disposable medical gowns during testing?",
            options: ["Yes", "No"]
        },
        {
            label: "Parental Consent",
            details: "For students, did the data entry staff verify parental consent prior to testing?",
            options: ["Yes", "No"]
        },
        {
            label: "Air Circulation",
            details: "Was there proper ventilation (i.e. outside air, open windows and doors, operating window or attic fans, or running air conditioner)?",
            options: ["Yes", "No"]
        },
        {
            label: "Test Process",
            details: "Was the test process conducted properly?",
            options: ["Yes", "No"]
        },
        {
            label: "Test Storage",
            details: "Was the manufacturer recommendation regarding test storage followed?",
            options: ["Yes", "No"]
        },
        {
            label: "Shipping",
            details: "Were the tests packaged and shipped according to the manufacturers recommendations?",
            options: ["Yes", "No"]
        },
        {
            label: "Testing Station",
            details: "Were the HCPs separating clean from dirty on their work area and during COVID-19 testing processes?",
            options: ["Yes", "No"]
        },
        {
            label: "Comments",
            details: "Other test site notes"
        },
    ];

    constructor() {
    }

    getQuestionAt(index: number): SurveyQuestion {
        return this.questions[index];
    }

    getQuestions(): SurveyQuestion[] {
        return this.questions;
    }
}
