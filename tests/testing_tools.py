
import psycopg2
import psycopg2.extras
import random






base = "http://localhost:8001/v0/"
conn = psycopg2.connect(database="mood", user="mooduser", password="password", host="localhost", port="8432")
conn.autocommit = True


def cursor():
	return conn.cursor(cursor_factory = psycopg2.extras.DictCursor)


def getObjectFromDB(table, id):
	depluralized = table
	if table[-1] == "s" and table not in ["series"]:
		depluralized = table[:-1]

	with cursor() as cur:
		cur.execute(f"SELECT * FROM {table} where {depluralized}id = {str(id)}")
		out = cur.fetchone()
	return out

def deleteObjectFromDB(table, id):
	depluralized = table
	if table[-1] == "s" and table not in ["series"]:
		depluralized = table[:-1]

	with cursor() as cur:
		cur.execute(f"DELETE FROM {table} where {depluralized}id = {str(id)}")


def existsInDB(table, id):
	depluralized = table
	if table[-1] == "s" and table not in ["series"]:
		depluralized = table[:-1]

	with cursor() as cur:
		cur.execute(f"SELECT * FROM {table} where {depluralized}id = {str(id)}")
		out = cur.fetchall()
	return out != []


def randomString():
	return "".join([random.choice("qwertyuioplkjhgfdsazxcvbnm") for i in range(0, 20)])


def randomValidTimestamp():

	year = str(random.randint(1995, 2021))
	month = str(random.randint(1, 12))
	if len(month) == 1: month = "0" + month
	day = str(random.randint(1, 28))
	if len(day) == 1: day = "0" + day

	hour = str(random.randint(0, 23))
	if len(hour) == 1: hour = "0" + hour
	minute = str(random.choice([0, 30]))
	if len(minute) == 1: minute = "0" + minute
	second = str(random.randint(0, 59))
	if len(second) == 1: second = "0" + second

	return f"{year}-{month}-{day}T{hour}:{minute}:{second}"


