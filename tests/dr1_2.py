import datetime
from time import time
import unittest
import requests
from requests.api import delete
from testing_tools import *



class DR1_2(unittest.TestCase):

	def test_series_get(self):
		# insert data into dw

		seriesid = 9999
		hostid = 1
		title = randomString()
		description = randomString()

		with cursor() as cur:
			cur.execute(f"""
			INSERT INTO series(seriesid, hostid, title, description)
			VALUES ({str(seriesid)}, {str(hostid)}, '{title}', '{description}') 
			""")
		
		# add some events to the series
		eventIDs = [9998, 9999]
		timeStarts = ['1999-01-08 04:05:06', '1999-01-09 04:05:06']

		for i in range(len(eventIDs) -1, -1, -1): # insert in reverse order - make sure they are returned in the correct order
			eventID = eventIDs[i]
			timeStart = timeStarts[i]
			with cursor() as cur:
				cur.execute(f"""
				INSERT INTO events(eventID, seriesID, title, description, timeStart, timeEnd)
				VALUES ({str(eventID)}, {str(seriesid)}, '{randomString()}', '{randomString()}', '{			timeStart}', '1999-01-08 04:05:06')
				""")


		response = requests.get(base + "series/" + str(seriesid))
		self.assertEqual(response.status_code//100, 2)

		try:
			js = response.json()

			self.assertIn("seriesID", js)
			self.assertIn("hostID", js)
			self.assertIn("eventIDs", js)
			self.assertIn("data", js)
			self.assertEqual(len(js), 4)
			self.assertIn("title", js["data"])
			self.assertIn("description", js["data"])
			self.assertEqual(len(js["data"]), 2)

			self.assertEqual(js["seriesID"], seriesid)
			self.assertEqual(js["hostID"], hostid)
			self.assertEqual(js["eventIDs"], eventIDs)
			self.assertEqual(js["data"]["title"], title)
			self.assertEqual(js["data"]["description"], description)

		finally:

			deleteObjectFromDB("series", seriesid)
			# cascading delete
	

	def test_series_valid_post(self):

		# strstmt = """
		# INSERT INTO series(seriesid, hostid, title, description)
		# VALUES ({}, {}, '{}', '{}') 
		# """

		test_suite = [
			(1, randomString(), randomString())
		]

		for hostid, title, description in test_suite:

			request_text = f"""{{
				"hostID": {str(hostid)},
				"data": {{
					"title": "{title}",
					"description": "{description}"
				}}
			}}"""

			response = requests.post(base + "series", request_text)
			seriesID = -1 # temp

			try:
				self.assertEqual(response.status_code//100, 2)

				js = response.json()
				self.assertIn("seriesID", js)
				self.assertEqual(len(js), 1)

				seriesID = js["seriesID"]

				# check added to database
				self.assertTrue(existsInDB("series", seriesID))
				row = getObjectFromDB("series", seriesID)
				self.assertEqual(row["seriesid"], seriesID)
				self.assertEqual(row["hostid"], hostid)
				self.assertEqual(row["title"], title)
				self.assertEqual(row["description"], description)

			finally:

				deleteObjectFromDB("series", seriesID)
	
	def test_series_valid_put(self):

		# title, description
		test_suite = [
			(randomString(), randomString())
		]

		for new_title, new_description in test_suite:

			## insert sample series
			seriesid = 9999
			hostid = 1
			title = randomString()
			description = randomString()

			with cursor() as cur:
				cur.execute(f"""
				INSERT INTO series(seriesid, hostid, title, description)
				VALUES ({str(seriesid)}, {str(hostid)}, '{title}', '{description}') 
				""")


			# attempt to edit series using put
			request_text = f"""{{
				"data" : {{
					"title": "{new_title}",
					"description": "{new_description}"
				}}
			}}"""
			response = requests.put(base + "series/" + str(seriesid), request_text)

			try:
				self.assertEqual(response.status_code//100, 2)

				# check the database has changed
				row = getObjectFromDB("series", seriesid)
				self.assertEqual(row["title"], new_title)
				self.assertEqual(row["description"], new_description)

			finally:

				deleteObjectFromDB("series", seriesid)
				pass

	
	def test_series_valid_delete(self):

		# insert series and events into db to check cascading delete (copied from the get)

		seriesid = 9999
		hostid = 1
		title = randomString()
		description = randomString()
		with cursor() as cur:
			cur.execute(f"""
			INSERT INTO series(seriesid, hostid, title, description)
			VALUES ({str(seriesid)}, {str(hostid)}, '{title}', '{description}') 
			""")
		
		# add some events to the series
		eventIDs = [9998, 9999]
		timeStarts = ['1999-01-08 04:05:06', '1999-01-09 04:05:06']
		for i in range(len(eventIDs) -1, -1, -1):
			eventID = eventIDs[i]
			timeStart = timeStarts[i]
			with cursor() as cur:
				cur.execute(f"""
				INSERT INTO events(eventID, seriesID, title, description, timeStart, timeEnd)
				VALUES ({str(eventID)}, {str(seriesid)}, '{randomString()}', '{randomString()}', '{			timeStart}', '1999-01-08 04:05:06')
				""")


		response = requests.delete(base + "series/" + str(seriesid))

		try:
			self.assertEqual(response.status_code//100, 2)
			self.assertFalse(existsInDB("series", seriesid))
			for eventID in eventIDs:
				self.assertFalse(existsInDB("events", eventID))
		
		finally:
			
			deleteObjectFromDB("series", seriesid)
			for eventID in eventIDs:
				deleteObjectFromDB("events", eventID)
			

	








	def insert_sample_series(self):

		seriesid = 9999
		hostid = 1
		title = randomString()
		description = randomString()
		with cursor() as cur:
			cur.execute(f"""
			INSERT INTO series(seriesid, hostid, title, description)
			VALUES ({str(seriesid)}, {str(hostid)}, '{title}', '{description}') 
			""")
		
		return seriesid
	

	def insert_sample_event(self, seriesID):
		eventID = 9998
		timeStart = '1999-01-08 04:05:06'
		with cursor() as cur:
			cur.execute(f"""
			INSERT INTO events(eventID, seriesID, title, description, timeStart, timeEnd)
			VALUES ({str(eventID)}, {str(seriesID)}, '{randomString()}', '{randomString()}', '{timeStart}', '1999-01-08 04:05:06')
			""")
		return eventID



	def test_event_get(self):

		seriesID = self.insert_sample_series()

		# eventid, seriesid, title, description, timestart, timeend
		test_suite = [
			(9999, seriesID, randomString(), randomString(), '1999-01-08T04:05:06', '1999-01-08T05:05:06')
		]
		
		try :

			for eventid, _seriesid, title, description, timestart, timeend in test_suite:
				with cursor() as cur:
					cur.execute(f"""
					INSERT INTO events(eventID, seriesID, title, description, timeStart, timeEnd)
					VALUES ({str(eventid)}, {str(_seriesid)}, '{title}', '{description}', '{timestart}', '{timeend}')
					""")
				
				# add some event forms to this event
				forms_to_add = [1,2,3]
				event_form_ids = [9997, 9998, 9999]
				for i in range(0, len(forms_to_add)):
					formid = forms_to_add[i]
					event_form_id = event_form_ids[i]
					with cursor() as cur:
						cur.execute(f"""
						INSERT INTO eventforms(eventformid, eventid, formid, numinevent, isactive)
						VALUES ({str(event_form_id)}, {str(eventid)}, {str(formid)}, {str(i+1)}, TRUE)
						""")
				

				
				response = requests.get(base + "events/" + str(eventid))

				try:
					self.assertEqual(response.status_code/100, 2)

					js = response.json()
					self.assertIn("eventID", js)
					self.assertIn("seriesID", js)
					self.assertIn("formIDs", js)
					self.assertIn("eventFormIDs", js)
					self.assertIn("data", js)
					self.assertEqual(len(js), 5)
					self.assertIn("title", js["data"])
					self.assertIn("description", js["data"])
					self.assertIn("time-start", js["data"])
					self.assertIn("time-end", js["data"])
					self.assertEqual(len(js["data"]), 4)

					self.assertEqual(js["eventID"], eventid)
					self.assertEqual(js["seriesID"], _seriesid)
					self.assertEqual(js["formIDs"][0], 0) # general feedback
					self.assertEqual(js["formIDs"][1:], forms_to_add)
					self.assertEqual(js["eventFormIDs"][1:], event_form_ids)
					self.assertEqual(js["data"]["title"], title)
					self.assertEqual(js["data"]["description"], description)
					self.assertEqual(js["data"]["time-start"], timestart)
					self.assertEqual(js["data"]["time-end"], timeend)


				finally:
					deleteObjectFromDB("events", eventid)

		finally:
			deleteObjectFromDB("series", seriesID)
	

	def test_event_valid_post(self):

		seriesID = self.insert_sample_series()

		# seriesID, title, description, time-start, time-end
		test_suite = [
			(seriesID, randomString(), randomString(), randomValidTimestamp(), randomValidTimestamp())
		]

		try:
			for _seriesid, title, description, timestart, timeend in test_suite:

				request_text = f"""{{
					"seriesID": "{_seriesid}",
					"data": {{
						"title": "{title}",
						"description": "{description}",
						"time-start": "{timestart}",
						"time-end": "{timeend}"
					}}
				}}"""

				response = requests.post(base + "events", request_text)

				eventID = -1 #temp

				try:
					self.assertEqual(response.status_code//100, 2)
					
					js = response.json()
					self.assertIn("eventID", js)
					self.assertEqual(len(js), 1)

					eventID = js["eventID"]

					# check added to the database
					row = getObjectFromDB("events", eventID)
					self.assertEqual(row["seriesid"], seriesID)
					self.assertEqual(row["title"], title)
					self.assertEqual(row["description"], description)
					self.assertEqual(row["timestart"], datetime.datetime.strptime(timestart, '%Y-%m-%dT%H:%M:%S'))
					self.assertEqual(row["timeend"], datetime.datetime.strptime(timeend, '%Y-%m-%dT%H:%M:%S'))



				
				finally:
					deleteObjectFromDB("events", eventID)

		finally:
			deleteObjectFromDB("series", seriesID)
		



	def test_event_valid_put(self):

		seriesID = self.insert_sample_series()
		eventID = self.insert_sample_event(seriesID)

		# title, description, time-start, time-end
		test_suite = [
			(randomString(), randomString(), randomValidTimestamp(), randomValidTimestamp())
		]

		try:
			for new_title, new_description, new_timestart, new_timeend in test_suite:

				request_string = f"""{{
					"data": {{
						"title": "{new_title}",
						"description": "{new_description}",
						"time-start": "{new_timestart}", 
						"time-end": "{new_timeend}"
					}}
				}}"""

				response = requests.put(base + "events/"+ str(eventID), request_string)

				self.assertEqual(response.status_code//100, 2)

				row = getObjectFromDB("events", eventID)
				self.assertEqual(row["title"], new_title)
				self.assertEqual(row["description"], new_description)
				self.assertEqual(row["timestart"], datetime.datetime.strptime(new_timestart, '%Y-%m-%dT%H:%M:%S'))
				self.assertEqual(row["timeend"], datetime.datetime.strptime(new_timeend, '%Y-%m-%dT%H:%M:%S'))


		finally:

			deleteObjectFromDB("events", eventID)
			deleteObjectFromDB("series", seriesID)


	def test_valid_event_delete(self):

		seriesID = self.insert_sample_series()
		eventID = self.insert_sample_event(seriesID)

		response = requests.delete(base + "events/" + str(eventID))

		try:
			self.assertEqual(response.status_code//100, 2)
			# check it is deleted from the db
			self.assertFalse(existsInDB("events", eventID))
		
		finally:
			deleteObjectFromDB("events", eventID)
			deleteObjectFromDB("series", seriesID)








	# TODO check invalid stuff









unittest.main()