import os

def generate_project_tree(root_dir, prefix=''):
    tree = ''
    items = os.listdir(root_dir)
    items.sort()
    for index, item in enumerate(items):
        path = os.path.join(root_dir, item)
        if os.path.isdir(path):
            if index == len(items) - 1:
                tree += f"{prefix}└── {item}/\n"
                tree += generate_project_tree(path, prefix + '    ')
            else:
                tree += f"{prefix}├── {item}/\n"
                tree += generate_project_tree(path, prefix + '│   ')
        else:
            if index == len(items) - 1:
                tree += f"{prefix}└── {item}\n"
            else:
                tree += f"{prefix}├── {item}\n"
    return tree

if __name__ == "__main__":
    project_root = '/Users/lanceche/pythonproject'  # 项目根目录
    project_tree = generate_project_tree(project_root)
    with open('project_tree.md', 'w') as f:
        f.write(project_tree)