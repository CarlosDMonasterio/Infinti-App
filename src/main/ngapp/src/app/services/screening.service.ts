import {Injectable} from '@angular/core';
import {SurveyQuestion} from "../models/survey-question";

@Injectable({
    providedIn: 'root'
})
export class ScreeningService {

    private questions: SurveyQuestion[] = [
        {
            label: "",
            details: "Within the last 48 hours, I have experienced a fever at or over 100.4 F.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 48 hours, I have experienced chills.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 48 hours, I have experienced a sore throat.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 48 hours, I have experienced a cough.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 48 hours, I have experienced gastrointestinal issues such as diarrhea.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 48 hours, I have experienced nausea/vomiting.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 48 hours, I have experienced a shortness of breath/difficulty breathing.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 48 hours, I have experienced malaise.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 48 hours, I have experienced fatigue.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 48 hours, I have experienced persistent pain/pressure in the chest.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 48 hours, I have experienced nasal congestion/runny nose.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 48 hours, I have experienced a loss of taste or smell.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the last 48 hours, I have travelled outside of the USA.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "I have been isolating/quarantining because I tested positive for COVID-19 or I’m worried that I may be Sick with COVID-19.",
            options: ["Yes", "No"]
        },
        {
            label: "",
            details: "Within the past 14 days, I have been in close contact with someone exhibiting any of the symptoms listed in this questionnaire or someone who has tested positive for COVID-19.",
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
