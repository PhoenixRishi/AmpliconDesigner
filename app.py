import os

print("Current Folder:", os.getcwd())
from flask import Flask, render_template, request
import subprocess

app = Flask(__name__)

@app.route('/', methods=['GET', 'POST'])
def home():

    output = ""
    genome = ""
    target = ""

    if request.method == 'POST':

        genome = ''.join(request.form['genome'].upper().split())
        target = ''.join(request.form['target'].upper().split())
        try:

            result = subprocess.run(
                ["java", "AmpliconDesigner", genome, target],
                capture_output=True,
                text=True
            )

            if result.returncode == 0:
                output = result.stdout
            else:
                output = result.stderr

        except Exception as e:
            output = f">>> ERROR: Running Java Engine Failed.\nDetails: {e} <<<"

    return render_template('index.html', output=output, genome=genome, target=target)

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000)