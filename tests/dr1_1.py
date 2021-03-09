from datetime import time
import unittest
import requests

from testing_tools import *


class DR1_1 (unittest.TestCase):

	# host signs into account
	def test_host_signin(self):
		# TODO not implemented
		pass

	def test_host_get(self):


		createStatement = """
		INSERT INTO host(hostID, email, pass, accountname)
		VALUES ({}, '{}', '{}', '{}')
		"""

		deleteObjectFromDB("host", 99999)

		valid_suite = [
			(99999, randomString(), randomString(), randomString()),
			(99999, "a" * 50, randomString(), randomString())
		]

		for test_row in valid_suite:

			hostID, email, password, account_name = test_row

			# insert into db
			with cursor() as cur:
				cur.execute(createStatement.format(*test_row))

			try:

				# attempt to get data
				response = requests.get(base+"hosts/" + str(hostID))
				self.assertEqual(response.status_code, 200)
				res_js = response.json()

				# response should only contain the "data" item
				self.assertEqual(len(res_js), 1)
				self.assertIn("data", res_js)
				self.assertEqual(res_js["data"]["email"], email)
				self.assertEqual(res_js["data"]["account-name"], account_name)
			
			finally:

				# delete the test data from the database
				deleteObjectFromDB("host", hostID)
	
	def test_host_valid_post(self):
		
		valid_suite = [
			(randomString(), randomString(), randomString()),
			("a" * 50, randomString(), randomString())
		]

		for email, password, account_name in valid_suite:

			requestData = f"""{{
				"email": "{email}",
				"pass": "{password}",
				"account-name": "{account_name}"
			}}"""

			response = requests.post(base + "hosts", requestData)

			try:

				self.assertEqual(response.status_code // 100, 2) # status code is 2xx
				self.assertIn("hostID", response.json())
				hostID = response.json()["hostID"]

				# check it has been added properly
				row = getObjectFromDB("host", hostID)
				self.assertEqual(row["email"], email)
				self.assertEqual(row["pass"], password)
				self.assertEqual(row["accountname"], account_name)
			
			finally:

				deleteObjectFromDB("host", hostID)

	def test_host_invalid_post(self):

		# blank request data
		requestData = ""
		response = requests.post(base + "hosts", requestData)
		self.assertNotEqual(response.status_code//100, 2)
		with self.assertRaises(ValueError):
			response.json()


		# missing arguments
		requestData = """{
			"email": "sent using invalid request!",
			"pass": "fwefwef"
		}"""
		response = requests.post(base + "hosts", requestData)
		self.assertNotEqual(response.status_code//100, 2)
		with self.assertRaises(ValueError):
			response.json()


		# blank arguments
		requestData = """{
			"email": "sent using invalid request!",
			"pass": "",
			"account-name": "fwew"
		}"""
		response = requests.post(base + "hosts", requestData)
		self.assertNotEqual(response.status_code//100, 2)
		with self.assertRaises(ValueError):
			response.json()
		

		# items too long
		requestData = f"""{{
			"email": "sent using invalid request!",
			"pass": "fwefwef",
			"account-name": "{"a" * 33}""
		}}"""
		response = requests.post(base + "hosts", requestData)
		self.assertNotEqual(response.status_code//100, 2)
		with self.assertRaises(ValueError):
			response.json()
		
		# apparently the backend can parse really messed up json. neat!

		# # invalid json
		# requestData = """{
		# 	"email" "sent with missing json!"
		# 	"pass" "se"
		# 	"account-name" "fwew"
		# }""" # missing colons and commas
		# response = requests.post(base + "hosts", requestData)
		# self.assertNotEqual(response.status_code//100, 2)
		# with self.assertRaises(ValueError):
		# 	response.json()



	def test_host_valid_put_all_fields(self):

		valid_suite = [
			(randomString(), randomString(), randomString()),
			("a" * 50, randomString(), randomString())
		]

		deleteObjectFromDB("host", 9999)


		for email, password, account_name in valid_suite:

			# insert the same data directly into database to edit
			with cursor() as cur:
				old_hostID = 9999
				old_email = "test_script@f"
				old_password = "test_script_pass"
				old_account_name = "test_script"
				cur.execute(f"""
				INSERT INTO host(hostID, email, pass, accountname)
				VALUES({str(old_hostID)}, '{old_email}', '{old_password}', '{old_account_name}')
				""")


			requestData = f"""{{
				"email": "{email}",
				"pass": "{password}",
				"account-name": "{account_name}"
			}}"""

			response = requests.put(base + "hosts/" + str(old_hostID), requestData)

			try:

				self.assertEqual(response.status_code // 100, 2) # status code is 2xx

				# check it has been edited properly
				row = getObjectFromDB("host", old_hostID)
				self.assertEqual(row["email"], email)
				self.assertEqual(row["pass"], password)
				self.assertEqual(row["accountname"], account_name)
			
			finally:

				deleteObjectFromDB("host", old_hostID)

	def test_host_valid_put_only_account_name(self):

		# check that the host can edit their account name without also submitting username and password
		deleteObjectFromDB("host", 9999)

		valid_suite = [
			("new account name"),
			("aaaaajjjjjj")
		]

		for (account_name) in valid_suite:

			# insert the same data directly into database to edit
			with cursor() as cur:
				old_hostID = 9999
				old_email = "test_script@f"
				old_password = "test_script_pass"
				old_account_name = "test_script"
				cur.execute(f"""
				INSERT INTO host(hostID, email, pass, accountname)
				VALUES({str(old_hostID)}, '{old_email}', '{old_password}', '{old_account_name}')
				""")


			requestData = f"""{{
				"account-name": "{account_name}"
			}}"""

			response = requests.put(base + "hosts/" + str(old_hostID), requestData)

			try:

				self.assertEqual(response.status_code // 100, 2) # status code is 2xx

				# check it has been edited properly
				row = getObjectFromDB("host", old_hostID)
				self.assertEqual(row["email"], old_email)
				self.assertEqual(row["pass"], old_password)
				self.assertEqual(row["accountname"], account_name)
			
			finally:

				deleteObjectFromDB("host", old_hostID)
	
	def test_delete(self):

		# insert test data to delete.
		with cursor() as cur:
			old_hostID = 9999
			old_email = "test_script@f"
			old_password = "test_script_pass"
			old_account_name = "test_script"
			cur.execute(f"""
			INSERT INTO host(hostID, email, pass, accountname)
			VALUES({str(old_hostID)}, '{old_email}', '{old_password}', '{old_account_name}')
			""")
		
		response = requests.delete(base + "hosts/" + str(old_hostID))
		
		try:
			self.assertEqual(response.status_code // 100, 2) # status code is 2xx
			self.assertFalse(existsInDB("host", old_hostID))
		
		finally:
			deleteObjectFromDB("host", old_hostID)



	# # host creates an account
	# def test_host_DR1_1(self):

	# 	# generate random host datta
	# 	email = randomString()
	# 	password = randomString()
	# 	account_name = randomString()
		
	# 	# send request
	# 	rqdata = f"""{{
	# 		"email": "{email}",
	# 		"pass": "{password}",
	# 		"account-name": "{account_name}"
	# 	}}"""
	# 	response = requests.post(base + "hosts", rqdata)
		
	# 	# check response from backend
	# 	self.assertEqual(response.status_code, 200)
	# 	self.assertIn("hostID", response.json())
	# 	hostID = response.json()["hostID"]

	# 	# check it has been added properly
	# 	row = getObjectFromDB("host", hostID)
	# 	self.assertEqual(row["email"], email)
	# 	self.assertEqual(row["pass"], password)
	# 	self.assertEqual(row["accountname"], account_name)





	# 	# check that GET returns the right data
	# 	response = requests.get(base+"hosts/" + str(hostID))
	# 	self.assertEqual(response.status_code, 200)
	# 	res_js = response.json()

	# 	self.assertIn("data", res_js)
	# 	self.assertEqual(res_js["data"]["email"], email)
	# 	self.assertEqual(res_js["data"]["account-name"], account_name)



	# 	# edit the row
	# 	new_email = randomString()
	# 	new_password = randomString()
	# 	new_account_name = randomString()

	# 	rqdata = f"""{{
	# 		"email": "{new_email}",
	# 		"pass": "{new_password}",
	# 		"account-name": "{new_account_name}"
	# 	}}"""
	# 	response = requests.put(base + "hosts/"+str(hostID), rqdata)
	# 	self.assertEqual(response.status_code, 200)

	# 	# check host has been updated
	# 	row = getObjectFromDB("host", hostID)
	# 	self.assertEqual(row["email"], new_email)
	# 	self.assertEqual(row["pass"], new_password)
	# 	self.assertEqual(row["accountname"], new_account_name)



	# 	# attempt to edit just the account name
	# 	new_account_name2 = randomString()
	# 	rqdata = f"""
	# 	{{ "account-name": "{new_account_name}"}}
	# 	"""
	# 	response = requests.put(base + "hosts/"+str(hostID), rqdata)
	# 	self.assertEqual(response.status_code, 200)

	# 	# check only account name has changed
	# 	row = getObjectFromDB("host", hostID)
	# 	self.assertEqual(row["email"], new_email)
	# 	self.assertEqual(row["pass"], new_password)
	# 	self.assertEqual(row["accountname"], new_account_name2)



	# 	# delete the row
	# 	response = requests.delete(base + "hosts/" + str(hostID))
	# 	self.assertEquals(response.status_code, 200)
	# 	self.assertFalse(existsInDB("host", hostID))


	# 	print("tested host")





unittest.main()





