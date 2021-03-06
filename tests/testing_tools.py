
import psycopg2
import psycopg2.extras
import random






base = "http://localhost:8001/v0/"
conn = psycopg2.connect(database="mood", user="mooduser", password="password", host="localhost", port="8432")

def cursor():
	return conn.cursor(cursor_factory = psycopg2.extras.DictCursor)


def getObjectFromDB(table, id):
	depluralized = table
	if table[-1] == "s" and table != "events":
		depluralized = table[:-1]

	with cursor() as cur:
		cur.execute(f"SELECT * FROM {table} where {depluralized}id = {str(id)}")
		out = cur.fetchone()
	return out

def existsInDB(table, id):
	depluralized = table
	if table[-1] == "s" and table != "events":
		depluralized = table[:-1]

	with cursor() as cur:
		cur.execute(f"SELECT * FROM {table} where {depluralized}id = {str(id)}")
		out = cur.fetchall()
	return out != []


def randomString():
	return "".join([random.choice("qwertyuioplkjhgfdsazxcvbnm") for i in range(0, 20)])