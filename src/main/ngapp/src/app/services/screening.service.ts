import {Injectable} from '@angular/core';
import {SurveyQuestion} from "../models/survey-question";

@Injectable({
    providedIn: 'root'
})
export class ScreeningService {

    private questions: SurveyQuestion[] = [
        {
            label: "",
            details: "Within the last 10 days I’ve experienced a fever at or over 100.4 F.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 10 days I’ve experienced chills.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 10 days I've experienced a sore throat.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 10 days I’ve experienced a cough.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 10 days I've experienced gastrointestinal issues such as diarrhea.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 10 days I’ve experienced a shortness of breath/difficulty breathing.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 10 days I’ve experienced a loss of taste or smell.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the past 10 days, I have been in close contact with someone exhibiting any of the symptoms listed in this questionnaire or someone who has tested positive for COVID.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the past 14 days, I have been informed by a medical provider that I have COVID-19.",
            options: ["Yes", "No"]
        },
    ]

    constructor() {
    }

    getQuestions(): SurveyQuestion[] {
        return this.questions;
    }
}
