*** New developer's guidelines for installing and configuring all required dependencies to run Infiniti Health's web app.***

- Guidelines Version 1.0 - January 2022
- All version up to the date of these instructions.  Please confirm with your supervisor for any version changes prior to installation.

- Download and install JAVA JDK 16.0.2
  - 
  - https://www.oracle.com/java/technologies/javase/jdk16-archive-downloads.html

- Download and Install Node.js: 16.13.2 (LTS)
  - 
    - https://nodejs.org/en/

- Install Angular CLI globally
  - 
    - Open Powershell as Administrator
    - Type: npm install -g @angular/cli and press enter
      - Current Angular CLI version is 13.1.2, the app will use local version is 12.2.14.
    - Type: Set ExecutionPolicy -ExecutionPolicy Unrestricted and press enter

- Create a BitBucket account
  - 
    - https://bitbucket.org/
    - Ensure your administrator has provided your account with access to Infini Health's repository.
      - https://bitbucket.org/infiniti-health/emr/src/main/
    - Create a branch with your name, clone the code and check out the branch.

- Download Intellij IDEA - Community Edition
  - 
   - https://www.jetbrains.com/idea/
   - Open Intellij, click on Get from VCS, paste the clone project address from BitBucket and clone the project.
   - About IntelliJ:
       - IntelliJ's strength is JAVA development and the preferred IDE. We highly recommend using it, but you may also use the IDE you are most comfortable with.  Keep in mind, other IDEs may change code formatting which may create a conflict when multiple developers work on the same code.
         - White text files are unchanged with branch
         - Blue text files are modified from branch
         - Green text files are new files
   - In-Depth IntelliJ IDEA instructions can be found here:
     - https://www.jetbrains.com/help/idea/discover-intellij-idea.html
   - Please ask as many questions as you need.

- Running and Developing the APP
  - 
   - From IntelliJ IDea, Open the project.
   - Inside scr -> main -> java, ou will find DevServer.  Right-click and run or run in Debug mode.  You will now have a DEVServer button on the top right controls in IntelliJ IDEA.
   - If successful, it will generate an ADMIN password.
   - On the bottom controls, click on terminal, click the down arrow and set the terminal default path to the ngapp directory "C:\Users\USERNAME\Documents\Repositories\Infiniti Health\emr\src\main\ngapp" - Your full path will differ based on the location of you repository.
   - Open a new local terminal windows (press +) and confirm the path default to ngapp directory, type: ng serve and press enter.
   - The application will build and provide you with the localhost address for the application:
     - ** Angular Live Development Server is listening on localhost:4200, open your browser on http://localhost:4200/ **
   - Changes in Angular to HTML, CSS and Typescript (TS) files are automatically updated in the browser in real time.
   - Changes to Java files require the DevServer to be stopped and ran again.
   - Most CSS used in the app comes from Bootstrap 4. Small localized CSS changes are authorized, but generally the site style should remain global.
     - https://getbootstrap.com/docs/4.0/getting-started/introduction/
   - Graphics and icons are puled from FontAwesome
     - https://fontawesome.com/
   - Insure graphics used, if not coming from FontAwesome are royalty free, for commercial user, with or without modification.

- Pushing and Pulling files
  - 
   - Insure you are ALWAYS pulling and PUSHING files to and from your branch. NEVER push directly to MAIN.
     - COMMIT any new or modified changes before pushing.
     - PUSH your changes to your branch by pressing the grene push arrow in the top right GIT controls - confirm the destination is your branch.
     - After the PUSH, go to BitBucket and create a pull from your branch to MAIN and add Hector Plahar as a reviewer unless otherwise told differently.
     - While you wait for your pull to be approved.  You may continue working on other tasks / code.
     - After your pull is approved, create a pull from MAIN to your branch and merge it.  This will update your branch with the latest production code.
     - In IntelliJ IDEA, pull (update code) by pressing the down arrow in the top right CIT controls and merge all new changes from your updated branch.

- Known H2 database issues
  - 
   - Production code runs with H2 database 2.0.  For local development it is ok to keep version 1.4 by changing the dependency version in POM.XML.
   -     <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>1.4.200</version>
         </dependency>
- No other changes to POM.XML have been authorized.
- Do not check in this change to your branch.  This is for LOCAL DEVELOPMENT ONLY.

If you have any questions please feel free to contact:
Hector Plahar: hector.plahar@infinitihealth.org
Carlos Monasterio: carlos.monasterio@infinitiheal.org
Yared Demissie: yared.demissie@infinitihealth.org

**Happy Coding and Welcome onboard.**