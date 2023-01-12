export class Unit {
    public x: number;
    public y:number;
    public colspan: number = 1;
    public rowspan: number = 1;
    public show: boolean = true;
    public size: string = '';
    public tree: any = new Tree(); // 树的信息
    public setData: string = ''; // 单元配置信息
}

export class Tree {
    public id: string = ''; // 单元id
    public label: string = ''; // 单元名称
    public size: string = '1X1';
}
