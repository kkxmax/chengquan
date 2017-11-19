//
//  HomeChoiceBusinessTableViewCell.m
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeChoiceBusinessTableViewCell.h"

@implementation HomeChoiceBusinessTableViewCell {
}
@synthesize lblName, extendButton, viewContent, contentTableView;
- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

#pragma mark - IBAction
- (IBAction)extendAction:(id)sender {
    if(extendButton.isSelected) {
        extendButton.selected = NO;
        viewContent.hidden = YES;
    }else{
        extendButton.selected = YES;
        viewContent.hidden = NO;
    }
    [contentTableView reloadData];
}

- (IBAction)selectAction:(id)sender {
    if(extendButton.isSelected) {
        extendButton.selected = YES;
    }
    
}
@end
