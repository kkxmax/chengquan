//
//  ItemTableViewCell.m
//  chengxin
//
//  Created by common on 7/31/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ItemTableViewCell.h"
#import "Global.h"

@implementation ItemTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    
}

-(IBAction)onEdit:(id)sender
{
    [self.delegate itemEditing:self];
}
-(IBAction)onDelete:(id)sender
{
    [self.delegate itemDeleting:self];
}
-(IBAction)onUpDown:(id)sender
{
    [self.delegate itemUpDown:self];
}
-(void)setIsUp:(BOOL)isUp
{
    _isUp = isUp;
    self.btnEdit.hidden = isUp;
    self.btnDelete.hidden = isUp;
    self.btnEdit.layer.cornerRadius = 14;
    self.btnEdit.layer.borderWidth = 1;
    self.btnEdit.layer.borderColor = [UIColor grayColor].CGColor;
    self.btnDelete.layer.cornerRadius = 14;
    self.btnDelete.layer.borderWidth = 1;
    self.btnDelete.layer.borderColor = [UIColor grayColor].CGColor;
    self.btnUpDown.layer.cornerRadius = 14;
    self.btnUpDown.layer.borderWidth = 1;
    self.btnUpDown.layer.borderColor = [UIColor grayColor].CGColor;
    if(isUp)
    {
        [self.btnUpDown setTitle:@"下架" forState:UIControlStateNormal];
        self.btnUpDown.layer.borderColor = [UIColor grayColor].CGColor;
        [self.btnUpDown setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        self.status.text = @"已上架";
        [self.status setTextColor:RGB_COLOR(255, 128, 0)];
    }else
    {
        [self.btnUpDown setTitle:@"上架" forState:UIControlStateNormal];
        self.btnUpDown.layer.borderColor = RGB_COLOR(255, 128, 0).CGColor;
        [self.btnUpDown setTitleColor:RGB_COLOR(255, 128, 0) forState:UIControlStateNormal];
        self.status.text = @"已下架";
        [self.status setTextColor:[UIColor blackColor]];
    }
}
@end
