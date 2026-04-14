from flask import Flask, render_template, request
import subprocess

app = Flask(__name__)

@app.route('/', methods=['GET', 'POST'])
def home():
    output = ""
    if request.method == 'POST':
        genome = request.form['genome']
        target = request.form['target']
        
        # This calls your compiled C++ program and catches what it prints
        try:
            result = subprocess.run(['./primer_engine.exe', genome, target], capture_output=True, text=True)
            output = result.stdout
        except Exception as e:
            output = f">>> ERROR: Running C++ Engine Failed. Did you compile primer_engine.cpp?\nDetails: {e} <<<"
            
    return render_template('index.html', output=output)

if __name__ == '__main__':
    app.run(debug=True)