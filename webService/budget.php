<?php
	if(isset($_POST['selectFn'])){
		$funct = $_POST['selectFn'];
		
		if($funct == "saveEstimate"){
			if(isset['month'] && isset['estimate']){
				
				$servername = "localhost";
				$dbname = "homebudget";
				$username = "root";
				$password = "";
				
				$conn = mysqli_connect($servername,$username,$password,$dbname);
			
				if(!$conn){
					die("Connection failed: " . mysqli_connect_error());
				}else{
					$month = $_POST['month'];
					$estimate = $_POST['estimate'];
					
					$qry = "INSERT INTO budget(month,estimate) VALUES ('$month','$estimate');";
					
					if(mysqli_query($conn,$qry)){
						echo "Success save estimate!!!";
					}else{
						echo "Fail to save, something wrong!?";
					}
				}
				mysqli_close($conn);	
				
			}else{
				echo "Month or Estimate not passed.";
			}
		}else if($funct == "calEstimate"){
			if(isset['month'] && isset['actual']){
				
				
				$servername = "localhost";
				$dbname = "homebudget";
				$username = "root";
				$password = "";
				
				$conn = mysqli_connect($servername,$username,$password,$dbname);
			
				if(!$conn){
					die("Connection failed: " . mysqli_connect_error());
				}else{
					$month = $_POST['month'];
					$actual = $_POST['actual'];
					$status = "";
					
					$qry = "SELECT estimate FROM budget WHERE month='$month'";
					
					$result = mysqli_query($conn,$qry);
					
					if(mysqli_num_rows($result)>0){
						while($row = mysqli_fetch_assoc($result)){
							$estimate = $row["estimate"];
							if($actual >= $estimate){
								$status = "Over budget?!";
							}else{
								$status = "Remain in budget.";
							}
						}
						echo $status;
					}else{
						echo "There are logs in that date.";
					}
				}
				mysqli_close($conn);
				
			}else{
				echo "Month or Actual not passed";
			}
		}
		
	}else{
		
		echo "No function";
	}

?>