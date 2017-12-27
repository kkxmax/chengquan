//
//  EvalTopTableViewCell.m
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "EvalTopTableViewCell.h"
#import "Global.h"

@implementation EvalTopTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.allSideButton.layer.cornerRadius = self.allSideButton.frame.size.height - 10;
    self.frontSideButton.layer.cornerRadius = self.frontSideButton.frame.size.height - 10;
    self.backSideButton.layer.cornerRadius = self.backSideButton.frame.size.height - 10;
    [self.allSideButton setSelected:YES];
    self.allSideButton.backgroundColor = RGB_COLOR(255, 166, 40);
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setNoSelectedButtons {
    self.allSideButton.selected = NO;
    self.allSideButton.backgroundColor = RGB_COLOR(245, 245, 245);
    self.frontSideButton.selected = NO;
    self.frontSideButton.backgroundColor = RGB_COLOR(245, 245, 245);
    self.backSideButton.selected = NO;
    self.backSideButton.backgroundColor = RGB_COLOR(245, 245, 245);
}
#pragma mark - IBAction
- (IBAction)onAllSideAction:(id)sender {
    [self setNoSelectedButtons];
    self.allSideButton.selected = YES;
    self.allSideButton.backgroundColor = RGB_COLOR(255, 166, 40);
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_ALLFRONTBACK_VIEW_NOTIFICATION object:[NSString stringWithFormat:@"%d", 0]];
}

- (IBAction)onFrontSideAction:(id)sender {
    [self setNoSelectedButtons];
    self.frontSideButton.selected = YES;
    self.frontSideButton.backgroundColor = RGB_COLOR(255, 166, 40);
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_ALLFRONTBACK_VIEW_NOTIFICATION object:[NSString stringWithFormat:@"%d", 1]];
}

- (IBAction)onBackSideAction:(id)sender {
    [self setNoSelectedButtons];
    self.backSideButton.selected = YES;
    self.backSideButton.backgroundColor = RGB_COLOR(255, 166, 40);
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_ALLFRONTBACK_VIEW_NOTIFICATION object:[NSString stringWithFormat:@"%d", 2]];
}
@end
