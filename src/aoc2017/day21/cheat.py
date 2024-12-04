def rotate(grid):
    grid = [list(line) for line in grid]
    size = len(grid)
    return [''.join([grid[size - r - 1][c] for r in range(size)]) for c in range(size)]

def grid_versions(grid):
    flip = [s[::-1] for s in grid]
    versions = {'/'.join(grid): 0, '/'.join(flip): 4}
    for i in range(3):
        grid = rotate(grid)
        flip = rotate(flip)
        versions.update({'/'.join(grid): i + 1, '/'.join(flip): i + 5})
    return versions

def find_rule(key, rules):
    for version, rotation in grid_versions(key).items():
        if version in rules:
            return rules[version].split('/')
    raise ValueError(f'No rule found for {key}')

def solve(grid, rules, iterations=5):
    for i in range(iterations):
        print('-----')
        print(grid)
        size = len(grid)
        new_grid = []
        if size % 2 == 0:
            sub_size = 2
        else:
            sub_size = 3
        for r in range(size // sub_size):
            new_grid.extend([''] * (sub_size + 1))
            for c in range(size // sub_size):
                sub_grid = [line[sub_size * c:sub_size * c + sub_size]
                           for line in grid[sub_size * r:sub_size * r + sub_size]]
                extended = find_rule(sub_grid, rules)
                for j in range(sub_size + 1):
                    new_grid[j - sub_size - 1] += extended[j]
        grid = new_grid

    print('-----')
    print(grid)
    return ''.join(grid).count('#')

if __name__ == '__main__':
    start = '.#./..#/###'.split('/')
    my_rules = {}
    with open('day21.in') as f:
        for line in f:
            before, after = line.rstrip().split(' => ')
            my_rules[before] = after

    sample_rules = {
        '../.#': '##./#../...',
        '.#./..#/###': '#..#/..../..../#..#'
    }

    # assert solve(start, sample_rules, 2) == 12
    print('Part 1', solve(start, my_rules, 3))
    print('Part 2', solve(start, my_rules, 5))
