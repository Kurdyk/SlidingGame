import math

import matplotlib.pyplot as plt
from matplotlib.patches import Rectangle
import os

def parse_file(file):
    result = dict()  # size, alogorithm, heuristic, shuffle_depth, solution_lenght, runtime, max_frontier, expansions
    for line in file:
        split_line = line.split(": ")
        result[split_line[0].replace(" ", "_")] = (split_line[1].replace(" ", "_").strip("\n"))
    return result


def read_all_size(size):
    result = list()
    directory_name = f"experiments/{size}x{size}"
    # List all the files in the directory
    filenames = os.listdir(directory_name)

    # Iterate over the files
    for filename in filenames:
        # Open the file and read its contents
        with open(os.path.join(directory_name, filename)) as f:
            content = parse_file(f)
            result.append(content)
    return result


def filter_by_depth(lower_bound, upper_bound, data):
    result = list()
    for dictio in data:
        if dictio["Shuffled_Depth"] == "Random":
            continue
        if lower_bound <= int(dictio["Shuffled_Depth"]) < upper_bound:
            result.append(dictio)
    return result


def filter_by_algo(algo, all_data):
    result = list()
    for data in all_data:
        if data["Algorithm"] == algo and data["Heuristic"] == "Manhattan_Distance":
            result.append(data)
    return result


def find_min_max(data_set, parameter, error_rate):
    if len(data_set) == 0:
        return -1, -1
    values = list()
    for data in data_set:
        values.append(int(data[parameter]))
    to_remove = int(error_rate * len(values))
    values.sort()
    return values[to_remove], values[len(values) - to_remove - 1]


def compare_algorithm():
    size = input("On what size do you want to compare the algorithms?\n")
    parameter = input("On what paramater do you want to compare the algorithms? "
                      "Runtime_(millis)/Max_Frontier_Size/Number_Of_Expansions\n")
    error_rate = float(input("Wanted error rate? 0.05 is recommanded\n"))

    color_per_algo = {"A*": "green", "IDA*": "red", "GreedyA*": "blue"}

    # Create a figure and axis
    fig, ax = plt.subplots()

    max_y = -math.inf

    for algo in color_per_algo.keys():

        print(f"Algotithm = {algo}")

        all_data = read_all_size(size)
        all_data = filter_by_algo(algo, all_data)

        i = 0
        while i < 10:
            lower_b = 50 * i
            upper_b = 50 * (1 + i)
            data_set = filter_by_depth(lower_b, upper_b, all_data)
            mini, maxi = find_min_max(data_set, parameter, error_rate)
            print(i, mini, maxi)
            if mini == -1:
                i += 1
                continue
            if maxi > max_y:
                max_y = maxi
            x0, y0, x1, y1 = lower_b, mini, upper_b, maxi

            # Add the rectangle to the axis
            rect = Rectangle((x0, y0), x1-x0, y1-y0, facecolor=color_per_algo[algo], edgecolor="black",
                             alpha=0.4)
            ax.add_patch(rect)
            i += 1

    # Set the limits of the axis
    ax.set_xlim([0, 50 * (i + 1)])
    ax.set_ylim([0, max_y])

    # Show the plot
    plt.show()


if __name__ == "__main__":
    compare_algorithm()
