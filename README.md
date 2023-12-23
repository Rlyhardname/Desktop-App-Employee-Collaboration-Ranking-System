# Desktop-App-Employee-Collaboration-Ranking-System

Table of contents:
1. Task and specific requirements  
2. Explanation of my understanding of the problem and the algorithm I used.  
3. DEMO(Will be filmed by the end of 25.12.2023).  


## TASK:  
### Pair of employees who have worked together   
#### Create an application that identifies the pair of employees who have 
#### worked together on common projects for the longest period of time
#### and the time for each of those projects.  
  
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
  
## Algorithms I used.  
  
I implemented various filtering stages of the data, to teset if it's elidgeable to be filtered and visualized as well as being coherent with our database so it can be stored.
After filtration I Map the records to a Map of employees and A list of employees where I compare Every employee with all his projects to every othee employee with all his projects using Map.contains() to avoid iteration over the list of projects every employee has.
When Two employees contain the same project the program does some checks if the time span of the employees on the project aligns and if 1 day or more they've worked together I return a date. This gets repeated until One employee has checked all other employees and projects
and everytime there is a match, we add the days to that employee's max days worked on projects with someone. After first employee iterates, we do swap the second with the first, and do the same, to get all his data points untill all employees swap with index 0 and gather their
dataset. Every employee skips his starting index(before the swap) when iterating to avoid matching himself. Everytime at least 1 day matched we add the data to list<DTO> object that holds the references to the 2 employees and the time they worked on a project. In the end 
we end up with a dataset with all they matches with all the pairs and the dates per project. Then we run a sort of the dataset by days worked together fill the data to our table to visualize in descending order.  
  
## DEMO: (In the next few days)  

