/*const Pool = require('pg').Pool
const pool = new Pool({
  user: 'postgres',
  host: 'localhost',
  database: 'specitems',
  password: 'Mina2003',
  port: 5432,
});

const getItems = () => {
    return new Promise(function(resolve, reject) {
      pool.query('SELECT * FROM spec_item_entity', (error, results) => {
        if (error) {
          reject(error)
        }
        resolve(results.rows);
      })
    }) 
  }

  module.exports = {
    getItems
  }*/