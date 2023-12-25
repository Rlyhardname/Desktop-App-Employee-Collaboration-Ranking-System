# Desktop-App-Employee-Collaboration-Ranking-System

## Table of contents:
1. Task and specific requirements  
2. Explanation of my understanding of the problem and the algorithm I used.  
3. DEMO(Will be filmed by the end of 25.12.2023).
4. INSTRUCTIONS!
5. Known issues


## TASK:  
### Pair of employees who have worked together. Create an application that identifies the pair of employees who have worked together on common projects for the longest period of time and the time for each of those projects.  

  
## Requirements:  
1. DateTo can be NULL, equivalent to today.  
2. We are interested in the number of days they worked together.  
3. The input data must be loaded to the program from a CSV file.  
4. More than one date format to be supported, extra points will be given if all. 
date formats are supported.  
5. In a README.md file summarize your understanding for the task and your
algorithm.  
6. Do not use external libraries CSV parsing.  
7. Follow clean code conventions.  
8. Implement persistence of the data.  
9. CRUD for Employees.  
  
## Explanation of my understanding of the problem.  
  
We will get CSV files with collumns representing empId, projectId, EmployeeStartProject and EmployeeQuit project. We should aim to extract and compare the data of the records so that every Employee that has a matching project with another employee
should be compared. If they have worked on a common project for any amount of time the the they we would extract the time they worked together, and repeat for every common project they have. The sum of all the data points of our too employees will give us
the total time they worked together on various projects. In the end we will rank every pair vs every other pair, and the pair with the longest work time on common project is #1 and would get displayed with a list of their individual projects and time on every single project as well
ass the total time they worked together.  
  
## Algorithms used.  

### Global/Main algorithm  

1. Read records and create Objects and if objects aren't null save to DB.
2. Optionally(radio button pick) check for referential integrity by checking if both employee key and project key exist. Depending on the current implementation, this step will be redundant based on DB architechture,configurations and constraints.
3. Map List<CSVRecords> -> Map<Long, Employees>
4. Map Map<Long, employees> -> List<Employees>
5. Inject two new datastructures in Service and start the filtering process.
6. Run for(i) on List<Employees> 0 to Size-1, compare 0 index to every other employee(date comparison algorithm(DCA)*), if they have a matching project and the (DCA)* return > 0 Create dataPoint with (emp1,emp2,days worked together on project). Add dataPoint to list<dataPoints>.
7. When index 0 from List<Employees> compares with every other index, switch indexies between index 1 and 0. This gets repeated for every employee, so on 3rd iteration index 2 goes to 0 and 0 goes to 2 ect... This way we get all datapoints, for every employee in our dataSet.
8. Sort dataSet. Depending if we want ascending or descending order later, we read from different end of dataset.
9. Populate dataGrid and return best result.

### DataComparison algorithm(DCA)  

From Global alg step 6 we find get 2 employees that have at least one matching project  
1. Start iterating over lists and comparing project until we find a matching one.
2. Compare the two projects start_date and leave_date to see if they overlap for at least one day and if true continue to next step else return 0 days and no dataPoint is added to dataSet.
3. Get four vars with both employee's current project being compared with start_time and leave_time of project.
4. Then we use e improvised state machine to check the four available states between the records and return Days.Between(emp1/emp2 start , emp1/2 leave) depending on the current state. This step always returns > 0 and therefor always gets added as a dataPoint to the dataSet  


## DEMO: (In the next few days)  

## INSTRUCTIONS:  

Go to GUI.CLASS and on the first lines of the constructor are the DB configurations. Change DB_Username and DB_password placeholder with the actual username and password of the database.

## Known Issues:  

1. If CRUD select_by_id might throw 2 popups instead of one if employee doesn't exists and he doesn't have any records also.. 

