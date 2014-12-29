import sqlite3

DB = None
CONN = None

def connect_to_db(): 
  global DB, CONN
  CONN = sqlite3.connect("pokedex.db")
  DB = CONN.cursor()

def main():
  connect_to_db()

  query = """SELECT * FROM pokemon"""
  DB.execute(query,)
  rows = DB.fetchall()

  for pokemon in rows:
    print "<string name=\"" + pokemon[1] + "\">" + pokemon[1].title() + "</string>";

  CONN.close()

if __name__ == "__main__":
  main()