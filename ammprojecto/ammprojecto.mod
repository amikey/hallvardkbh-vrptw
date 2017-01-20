/*********************************************
 * OPL 12.5.1.0 Model
 * Author: 1212887
 * Creation Date: 17/01/2017 at 14:18:10
 *********************************************/

 //Model inputs
int L=...; //Number of locations
int maxV = ...; //max amount of vehicles that can be used
range I=1..L; //1 = start node, 2....N = customer nodes
range J=1..L; //1 = start node, 2....N = customer nodes
range K=1..maxV; //max amount of vehicles possible
range A=2..L;
range C=1..11;
int mint[i in I]=...; //minimum arrival time
int maxt[i in I]=...; //maximum arrival time
int t[i in I]=...; //workload at each location
int M = ...; //given large value
int totalTimeLimit = ...; //max time = 720 min = 12h
int dist[i in I][j in J] = ...; //distances between all nodes 


//Model decision variables
dvar int z;
dvar int s_kj[k in K, j in J];
dvar int x_kij[k in K, i in I, j in J];




//pre script
execute {
writeln("Pre script currently running");
}; 
 
//Objective function 
minimize z;

subject to{


	//Constraint 1 - a vehicle needs to travel from one node to a different one
	forall(k in K){
		forall(i in I){
			x_kij[k][i][i] == 0;		
		}	
	}
	
	//Constraint 2 - Makes sure that there is only one vehicle going to a specific node
	forall(j in A){
		sum(k in K, i in I) x_kij[k][i][j] == 1 && sum(k in K, i in I) x_kij[k][j][i] == 1;
	}
	
	
	//Constraint 4 - Guarantees that each vehicle departs and returns to the depot
	forall(k in K){
		sum(j in A)x_kij[k][1][j] - sum(j in A)x_kij[k][j][1] == 0;	
	}
	
	//Constraint 9 - x is 1 if a vehicle travels between two given nodes, 0 otherwise
	forall(k in K){
		forall(i in I){
			forall(j in J){
				x_kij[k][i][j]	== 0 || x_kij[k][i][j] == 1;		
			}		
		}	
	}
	
	
	
	
	
	
	
	//Constraint 4 - Only one starting point for a car
	forall(k in K){
		sum(j in A)x_kij[k][1][j] <= 1;
	}
	
	
	
	//Constraint 5 - Can not go back to the node the vehicle came from
	forall(k in K){
	  forall (i in I){
	    forall (j in A){
			x_kij[k][i][j] + x_kij[k][j][i] <= 1;
		}
	  }
	}
	
	//Constraint 6 - Ensure time windows are observed - WORKS, BUT IS EQUAL TO minArrival LIST FOR NOW
	forall(k in K){
		forall(j in A){
			mint[j] <= s_kj[k][j] <= maxt[j];			
		}		
	}
	
	//Constraint 7 - a vehicle cannot arrive at the next location too soon	
	forall(k in K){
		forall(i in I){
			forall(j in J){
				(s_kj[k][i] + dist[i][j] + t[i] - L*(1-x_kij[k][i][j])) <= s_kj[k][j];			
			}		
		}	
	}
	
	//Constraint 10 - Makes you leave the node you came from	
	  forall (j in A){
	  	(sum(k in K, i in I)x_kij[k][i][j] == 1) + (sum(k in K, i in I)x_kij[k][j][i] == 1) == 2;
	  }
	
	
	
	
	
	
	
	//Constraint 8 - Each vehicle can only leave depot once
	forall(k in K){
		sum (i in A)x_kij[k][1][i] == 1;
		sum (j in A)x_kij[k][j][1] == 1;
	}
	
	
	
	
	
	
	
	
	/*
	
	//Constraint 8 - total time for each vehicle cannot exceed time limit
	forall(k in K){
		sum(i in I, j in J) x_kij[k][i][j]*(dist[i][j] + t[j]) <= totalTimeLimit;
	}
	
	/*
	
	//Constraint 9 - maximize amount of nodes per vehicle
	forall(k in K){
			sum(j in J)r_kj[k][j] <= sum(k in K, i in I, j in J)x_kij[k][i][j]; 			
		
	}
	*/
	//Constraint 10 - minimize the number of vehicles
	z >= sum(k in K, i in I, j in J) k * x_kij[k][i][j]*(dist[i][j] + t[j]);
	

}

//post script
execute {
	writeln("Post script currently running");
}; 