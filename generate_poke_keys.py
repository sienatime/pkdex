import sqlite3

DB = None
CONN = None

def connect_to_db(): 
  global DB, CONN
  CONN = sqlite3.connect("app/src/main/assets/databases/pokedex.sqlite")
  DB = CONN.cursor()

def main():
  connect_to_db()

  query = """SELECT encounter_method_id, name FROM encounter_method_prose WHERE local_language_id = 9"""
  DB.execute(query,)
  rows = DB.fetchall()

  for row in rows:
    if row[1]:
      print "<string name=\"encounter_method_" + str(row[0]) + "\">" + row[1] + "</string>";

  CONN.close()

if __name__ == "__main__":
  main()