<?php
	require_once $_SERVER['DOCUMENT_ROOT'] . '/Foundation/Config.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/Foundation/Database.php';

	//deal with the decode and encode function
	class UserManager {
		public static function loginOAuth($conn, $nid, $token) {
		    // sanitize
		    $nid = htmlspecialchars(strip_tags($nid));
		    $token = htmlspecialchars(strip_tags($token));

			$query = "INSERT INTO " . Config::$db_account . " (`account`,`token`) VALUES (:nid, :token) 
					  ON DUPLICATE KEY UPDATE token = :token";

			$stmt = $conn->prepare($query);

		    // bind values
		    $stmt->bindParam(":nid", $nid);
		    $stmt->bindParam(":token", $token);

			if($stmt->execute()) {
		    	return true;
		    }
		    else {
		    	return false;
		    }
		}

		public static function verifyUser($account, $token) {
			if(!isset($account) || !isset($token)) {
				return false;
			}

			$token = htmlspecialchars(strip_tags($token));
			$account = htmlspecialchars(strip_tags($account));

            $database = new Database();
            $conn = $database->getConnection();
            if(!isset($conn)) {
                return false;
            }

            $query = "SELECT * FROM " . Config::$db_account . " WHERE account = :account AND token = :token";
            $stmt = $conn->prepare($query);

            // bind values
		    $stmt->bindParam(":account", $account);
		    $stmt->bindParam(":token", $token);

		    if(!$stmt->execute()) {
    			$conn = null;
    			return false;
			}

			if ($stmt->rowCount() > 0) {
				$conn = null;
				return true;
			} 
			else {
				$conn = null;
				return false;
			}
		}
	}

?>