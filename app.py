from flask import Flask, render_template, request
import subprocess
import platform

app = Flask(__name__)

@app.route('/', methods=['GET', 'POST'])
def home():
    output = ""
    if request.method == 'POST':
        genome = request.form['genome']
        target = request.form['target']

        engine_path = './primer_engine.exe' if platform.system() == 'Windows' else './primer_engine'
        
        try:
            result = subprocess.run([engine_path, genome, target], capture_output=True, text=True)
            output = result.stdout
        except Exception as e:
            output = f">>> ERROR: Running C++ Engine Failed.\nDetails: {e} <<<"
            
    return render_template('index.html', output=output)

if __name__ == '__main__':
    app.run(debug=True)