import argparse
import os
import matplotlib.pyplot as plt

def read_files_in_directory(directory):
  # List all the files in the directory
    filenames = os.listdir(directory)
    parsed_experiments = list()

  # Iterate over the files
    for filename in filenames:
    # Open the file and read its contents
        with open(os.path.join(directory, filename)) as f:
            file_dict = {}
            for line in f:
                splitted = line.split(':')
                file_dict[splitted[0].rstrip()] = splitted[1].rstrip()
            parsed_experiments.append(file_dict)

    # Print the contents of the file
    return parsed_experiments

def graph_experiment(parsed_experiments):
    fig, ax = plt.subplots()
    x = list()
    y = list()
    for experiment in parsed_experiments:
        x.append(experiment['Heuristic'])
        y.append(experiment['Max Frontier Size'])
    
    ax.bar(x, y)
    ax.set_xlabel('Heuristic')
    ax.set_ylabel('Max Frontier Size')
    plt.show()

if __name__ == "__main__":
  # Parse the command-line arguments
  parser = argparse.ArgumentParser()
  parser.add_argument("directory", help="the directory to read the files from")
  args = parser.parse_args()

  # Read all the files in the specified directory
  graph_experiment(read_files_in_directory(args.directory))
