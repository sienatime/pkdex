import sqlite3

DB = None
CONN = None

def connect_to_db(): 
  global DB, CONN
  CONN = sqlite3.connect("app/src/main/assets/databases/pokedex.sqlite")
  DB = CONN.cursor()

def main():
  connect_to_db()

  query = """SELECT type_id, name FROM type_names WHERE local_language_id = 9"""
  DB.execute(query,)
  rows = DB.fetchall()

  for row in rows:
    print "<string name=\"type_" + str(row[0]) + "\">" + row[1] + "</string>";

  CONN.close()

if __name__ == "__main__":
  main()