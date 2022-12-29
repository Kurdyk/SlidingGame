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
                file_dict[splitted[0].rstrip().strip()] = splitted[1].rstrip().strip()
            parsed_experiments.append(file_dict)

    # Print the contents of the file
    return sorted(parsed_experiments, key=lambda item: item['Shuffled Depth'])

def graph_experiment(parsed_experiments):
    fig, ax = plt.subplots()
    ucs_y = list()
    ucs_x = list()
    displ_y = list()
    displ_x = list()
    manh_x = list()
    manh_y = list()

    ucs = 'Uniform Cost'
    displ = 'Displacement'
    mand = 'Manhattan Distance'

    for experiment in parsed_experiments:
        heuristic = experiment['Heuristic']
        if (heuristic == ucs):
            ucs_y.append(int(experiment['Runtime (millis)']))
            ucs_x.append(int(experiment['Shuffled Depth']))
        elif (heuristic == displ):
            displ_y.append(int(experiment['Runtime (millis)']))
            displ_x.append(int(experiment['Shuffled Depth']))
        elif (heuristic == mand):
            manh_x.append(int(experiment['Shuffled Depth']))
            manh_y.append(int(experiment['Runtime (millis)']))
    
    ucs_x.sort()
    ucs_y.sort()
    displ_x.sort()
    displ_y.sort()
    manh_x.sort()
    manh_y.sort()

    ax.plot(ucs_x, ucs_y, color="blue", label="UCS")
    ax.plot(displ_x, displ_y, color="green", label="Displacement")
    ax.plot(manh_x, manh_y, color="red", label="Manhattan Distance")

    ax.set_ylabel('Runtime (millis)')
    ax.set_xlabel('Shuffled Depth')
    plt.legend()
    plt.show()

if __name__ == "__main__":
  # Parse the command-line arguments
  parser = argparse.ArgumentParser()
  parser.add_argument("directory", help="the directory to read the files from")
  args = parser.parse_args()

  # Read all the files in the specified directory
  graph_experiment(read_files_in_directory(args.directory))
