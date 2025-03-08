from flask import Flask, request, jsonify
import mysql.connector
import os

app = Flask(__name__)

# MySQL connection details from environment variables
db_config = {
    'host': os.getenv('DB_HOST', 'localhost'),
    'user': os.getenv('DB_USER', 'root'),
    'password': os.getenv('DB_PASSWORD', 'rootpassword'),
    'database': os.getenv('DB_NAME', 'skill_discounts'),
    'port': int(os.getenv('DB_PORT', 3306))
}

def get_db_connection():
    return mysql.connector.connect(**db_config)

# Fetch rewards for a specific user
@app.route('/user_rewards/<int:user_id>', methods=['GET'])
def get_user_rewards(user_id):
    try:
        conn = get_db_connection()
        cursor = conn.cursor(dictionary=True)
        cursor.execute("""
            SELECT ur.business_id, b.name, ur.points, ur.wallet_balance
            FROM user_rewards ur
            JOIN businesses b ON ur.business_id = b.id
            WHERE ur.user_id = %s
        """, (user_id,))
        rewards = cursor.fetchall()
        cursor.close()
        conn.close()
        
        return jsonify(rewards), 200
    except mysql.connector.Error as err:
        return jsonify({"error": f"Database error: {err}"}), 500

# Redeem rewards for a specific business
@app.route('/redeem', methods=['POST'])
def redeem_points():
    data = request.json
    user_id = data.get('user_id')
    business_id = data.get('business_id')

    if not user_id or not business_id:
        return jsonify({"error": "Missing required fields"}), 400

    try:
        conn = get_db_connection()
        cursor = conn.cursor()

        # Check if user has enough points
        cursor.execute("""
            SELECT points FROM user_rewards WHERE user_id = %s AND business_id = %s
        """, (user_id, business_id))
        result = cursor.fetchone()

        if not result or result[0] < 250:
            return jsonify({"error": "Not enough points"}), 400

        # Deduct points and update wallet
        cursor.execute("""
            UPDATE user_rewards
            SET points = points - 250, wallet_balance = wallet_balance + 1
            WHERE user_id = %s AND business_id = %s
        """, (user_id, business_id))
        
        conn.commit()
        cursor.close()
        conn.close()

        return jsonify({"status": "success", "message": "Redemption successful"}), 200
    except mysql.connector.Error as err:
        return jsonify({"error": f"Database error: {err}"}), 500

# Add a new user
@app.route('/add_user', methods=['POST'])
def add_user():
    data = request.json
    username = data.get('username')

    if not username:
        return jsonify({"error": "Missing username"}), 400

    try:
        conn = get_db_connection()
        cursor = conn.cursor()

        # Insert new user
        cursor.execute("INSERT INTO users (username) VALUES (%s)", (username,))
        user_id = cursor.lastrowid

        # Initialize rewards for all businesses with 0 points and wallet balance
        cursor.execute("SELECT id FROM businesses")
        businesses = cursor.fetchall()
        for (business_id,) in businesses:
            cursor.execute(
                "INSERT INTO user_rewards (user_id, business_id, points, wallet_balance) VALUES (%s, %s, 0, 0)",
                (user_id, business_id)
            )

        conn.commit()
        cursor.close()
        conn.close()

        return jsonify({"status": "success", "user_id": user_id}), 201
    except mysql.connector.Error as err:
        return jsonify({"error": f"Database error: {err}"}), 500

# Initialize businesses (run once)
@app.route('/initialize_businesses', methods=['POST'])
def initialize_businesses():
    businesses = ["Pizza Place", "Hardware Store", "Antique Shop"]
    
    try:
        conn = get_db_connection()
        cursor = conn.cursor()

        for business in businesses:
            cursor.execute("INSERT IGNORE INTO businesses (name) VALUES (%s)", (business,))

        conn.commit()
        cursor.close()
        conn.close()

        return jsonify({"status": "success", "message": "Businesses initialized"}), 200
    except mysql.connector.Error as err:
        return jsonify({"error": f"Database error: {err}"}), 500

if __name__ == '__main__':
    app.run(debug=True, host="0.0.0.0", port=5000)
