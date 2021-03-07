import unittest
import requests

from testing_tools import *


class CR1 (unittest.TestCase):

	# host signs into account
	def test_host_signin(self):
		# TODO not implemented
		pass

	# host creates an account
	def test_host_DR1_1(self):

		# generate random host datta
		email = randomString()
		password = randomString()
		account_name = randomString()
		
		# send request
		rqdata = f"""{{
			"email": "{email}",
			"pass": "{password}",
			"account-name": "{account_name}"
		}}"""
		response = requests.post(base + "hosts", rqdata)
		
		# check response from backend
		self.assertEqual(response.status_code, 200)
		self.assertIn("hostID", response.json())
		hostID = response.json()["hostID"]

		# check it has been added properly
		row = getObjectFromDB("host", hostID)
		self.assertEqual(row["email"], email)
		self.assertEqual(row["pass"], password)
		self.assertEqual(row["accountname"], account_name)





		# check that GET returns the right data
		response = requests.get(base+"hosts/" + str(hostID))
		self.assertEqual(response.status_code, 200)
		res_js = response.json()

		self.assertIn("data", res_js)
		self.assertEqual(res_js["data"]["email"], email)
		self.assertEqual(res_js["data"]["account-name"], account_name)



		# edit the row
		new_email = randomString()
		new_password = randomString()
		new_account_name = randomString()

		rqdata = f"""{{
			"email": "{new_email}",
			"pass": "{new_password}",
			"account-name": "{new_account_name}"
		}}"""
		response = requests.put(base + "hosts/"+str(hostID), rqdata)
		self.assertEqual(response.status_code, 200)

		# check host has been updated
		row = getObjectFromDB("host", hostID)
		self.assertEqual(row["email"], new_email)
		self.assertEqual(row["pass"], new_password)
		self.assertEqual(row["accountname"], new_account_name)



		# attempt to edit just the account name
		new_account_name2 = randomString()
		rqdata = f"""
		{{ "account-name": "{new_account_name}"}}
		"""
		response = requests.put(base + "hosts/"+str(hostID), rqdata)
		self.assertEqual(response.status_code, 200)

		# check only account name has changed
		row = getObjectFromDB("host", hostID)
		self.assertEqual(row["email"], new_email)
		self.assertEqual(row["pass"], new_password)
		self.assertEqual(row["accountname"], new_account_name2)



		# delete the row
		response = requests.delete(base + "hosts/" + str(hostID))
		self.assertEquals(response.status_code, 200)
		self.assertFalse(existsInDB("host", hostID))


		print("tested host")







	def test_event_DR1_2(self):

		# generate event data
		seriesid = 1
		title = randomString()
		description = randomString()

		
		# # send request
		# rqdata = f"""{{
		# 	"email": "{email}",
		# 	"pass": "{password}",
		# 	"account-name": "{account_name}"
		# }}"""
		# response = requests.post(base + "hosts", rqdata)
		
		# # check response from backend
		# self.assertEqual(response.status_code, 200)
		# self.assertIn("hostID", response.json())
		# hostID = response.json()["hostID"]

		# # check it has been added properly
		# row = getObjectFromDB("host", hostID)
		# self.assertEqual(row["email"], email)
		# self.assertEqual(row["pass"], password)
		# self.assertEqual(row["accountname"], account_name)





		# # check that GET returns the right data
		# response = requests.get(base+"hosts/" + str(hostID))
		# self.assertEqual(response.status_code, 200)
		# res_js = response.json()

		# self.assertIn("data", res_js)
		# self.assertEqual(res_js["data"]["email"], email)
		# self.assertEqual(res_js["data"]["account-name"], account_name)



		# # edit the row
		# new_email = randomString()
		# new_password = randomString()
		# new_account_name = randomString()

		# rqdata = f"""{{
		# 	"email": "{new_email}",
		# 	"pass": "{new_password}",
		# 	"account-name": "{new_account_name}"
		# }}"""
		# response = requests.put(base + "hosts/"+str(hostID), rqdata)
		# self.assertEqual(response.status_code, 200)

		# # check host has been updated
		# row = getObjectFromDB("host", hostID)
		# self.assertEqual(row["email"], new_email)
		# self.assertEqual(row["pass"], new_password)
		# self.assertEqual(row["accountname"], new_account_name)



		# # attempt to edit just the account name
		# new_account_name2 = randomString()
		# rqdata = f"""
		# {{ "account-name": "{new_account_name}"}}
		# """
		# response = requests.put(base + "hosts/"+str(hostID), rqdata)
		# self.assertEqual(response.status_code, 200)

		# # check only account name has changed
		# row = getObjectFromDB("host", hostID)
		# self.assertEqual(row["email"], new_email)
		# self.assertEqual(row["pass"], new_password)
		# self.assertEqual(row["accountname"], new_account_name2)



		# # delete the row
		# response = requests.delete(base + "hosts/" + str(hostID))
		# self.assertEquals(response.status_code, 200)
		# self.assertFalse(existsInDB("host", hostID))


		# print("tested host")



unittest.main()





